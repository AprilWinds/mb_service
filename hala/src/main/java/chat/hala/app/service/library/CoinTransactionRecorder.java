package chat.hala.app.service.library;

import chat.hala.app.entity.*;
import chat.hala.app.library.util.Constant;
import chat.hala.app.repository.CoinTransactionRepository;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.RoomStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by astropete on 2018/3/9.
 */

@Component
public class CoinTransactionRecorder {

    @Autowired
    private CoinTransactionRepository ctRepo;


    @Autowired
    private RoomStyleRepository rsRepo;

    public void recordRecharge(Long memberId, Integer coinGet, Long referenceId, Boolean fromWA){
        CoinTransaction ct = new CoinTransaction();
        ct.setMemberId(memberId);
        ct.setAmount(coinGet);
        ct.setFromAction(CoinTransaction.FromAction.recharge);
        ct.setReferenceId(referenceId);
        ct.setThrough(fromWA ? "WA" : "RECHARGE");
        ct.setCreatedAt(new Date());
        ct.setUpdatedAt(new Date());
        ctRepo.saveAndFlush(ct);
    }

    public void recordChatGiftGiving(Long memberId, Integer price, GiftGiving giving){
        CoinTransaction ct = new CoinTransaction();
        ct.setMemberId(memberId);
        ct.setAmount(-price);
        ct.setFromAction(CoinTransaction.FromAction.gift);
        ct.setReferenceId(giving.getReceiverId());
        ct.setThrough(giving.inRoom() ? "ROOM"+String.valueOf(giving.getRoomId()) : "CHAT");
        ct.setCreatedAt(new Date());
        ct.setUpdatedAt(new Date());
        ctRepo.saveAndFlush(ct);
    }

    public void recordRoomSpent(Long memberId, Room r, CoinTransaction.FromAction action){

        CoinTransaction ct = new CoinTransaction();
       /* Map<Room.RoomStyle, Map> styles = Constant.getRoomStyle();
        Map<Map, Integer> styleParams = styles.get(r.getStyle());*/


        if(action == CoinTransaction.FromAction.onmic) ct.setAmount(-r.getMicrophonePrice());
        if(action == CoinTransaction.FromAction.insider) ct.setAmount(-r.getInsiderPrice());
        if(action == CoinTransaction.FromAction.addroom) ct.setAmount(rsRepo.findOne(r.getStyleId()).getCoin()/*styleParams.get("coin")*/);
        if(action == CoinTransaction.FromAction.upgraderoom) ct.setAmount(rsRepo.findOne(r.getStyleId()).getCoin()/*styleParams.get("coin")*/);
        ct.setFromAction(action);
        ct.setReferenceId(r.getId());
        ct.setMemberId(memberId);
        ct.setThrough("ROOM"+String.valueOf(r.getId()));
        ct.setCreatedAt(new Date());
        ct.setUpdatedAt(new Date());
        ctRepo.saveAndFlush(ct);
    }

    public void recordCheckInIncome(Long memberId, Integer income, Integer dayCount){
        CoinTransaction ct = new CoinTransaction();
        ct.setMemberId(memberId);
        ct.setAmount(income);
        ct.setFromAction(CoinTransaction.FromAction.checkin);
        ct.setReferenceId(memberId);
        ct.setThrough("CHECKIN" + String.valueOf(dayCount));
        ct.setCreatedAt(new Date());
        ct.setUpdatedAt(new Date());
        ctRepo.saveAndFlush(ct);
    }

    public void recordTaskIncome(Long memberId, Task task){
        CoinTransaction ct = new CoinTransaction();
        ct.setMemberId(memberId);
        ct.setAmount(task.getCoins());
        ct.setFromAction(CoinTransaction.FromAction.task);
        ct.setReferenceId(task.getId());
        ct.setThrough("TASK");
        ct.setCreatedAt(new Date());
        ct.setUpdatedAt(new Date());
        ctRepo.saveAndFlush(ct);
    }

}
