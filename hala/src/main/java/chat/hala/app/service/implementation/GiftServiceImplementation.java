package chat.hala.app.service.implementation;

import chat.hala.app.entity.*;
import chat.hala.app.library.RongCloud;
import chat.hala.app.library.util.Constant;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.Http;
import chat.hala.app.repository.*;
import chat.hala.app.service.GiftService;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import chat.hala.app.service.library.CoinTransactionRecorder;
import chat.hala.app.service.library.TaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by astropete on 2018/1/26.
 */

@Service
public class GiftServiceImplementation implements GiftService {

    @Autowired
    private GiftRepository giftRepo;

    @Autowired
    private GiftGivingRepository givingRepo;

    @Autowired
    private CoinTransactionRecorder ctRecorder;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TaskHandler taskHandler;

    @Override
    public Object getAllGift(String language){
        List<Gift> gifts =  giftRepo.findAllOrderByPriceAsc();
        for(Gift g : gifts){
            if(language.equals(Constant.LAN_ARAB)){
                g.setName(g.getArabicName());
            }
            g.setArabicName(null);
        }
        return gifts;
    }

    @Override
    public Object giveMemberGift(Member member, GiftGiving giving) throws PreconditionNotQualifiedException {
        Gift g = giftRepo.findById(giving.getGiftId());
        if(member.getCoins() < g.getPrice()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_ENOUGH_COINS);
        ctRecorder.recordChatGiftGiving(member.getId(), g.getPrice(), giving);
        giving.setCreatedAt(new Date());
        giving.setUpdatedAt(new Date());
        giving.setSenderId(member.getId());
        givingRepo.saveAndFlush(giving);
        if(giving.inRoom()){
            Room room = roomRepo.findOne(giving.getRoomId());
            room.setWealth(room.getWealth() + g.getPrice());
            room.setUpdatedAt(new Date());
            roomRepo.saveAndFlush(room);
            taskHandler.completeTask(member, "giftroom");
            if(givingRepo.findMemberGiftCountInRoom(member.getId()) > 10) taskHandler.completeTask(member, "giftroom10");
        }
        Member receiver = memberRepo.findOne(giving.getReceiverId());
        receiver.setWealth(receiver.getWealth() + g.getPrice());
        memberRepo.saveAndFlush(receiver);
        if(member.getId().equals(receiver.getId())){
            member.setWealth(receiver.getWealth());
        }
        member.setSpent(member.getSpent() + g.getPrice());
        member.setCoins(member.getCoins() - g.getPrice());
        memberRepo.saveAndFlush(member);
        return Http.coinResponse(g, member.getCoins());
    }
}
