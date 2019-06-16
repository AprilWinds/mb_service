package chat.hala.app.service.implementation;

import chat.hala.app.entity.Member;
import chat.hala.app.entity.Room;
import chat.hala.app.restful.wrapper.MemberAmount;
import chat.hala.app.entity.RoomEntering;
import chat.hala.app.library.util.General;
import chat.hala.app.repository.CoinTransactionRepository;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.RoomEnteringRepository;
import chat.hala.app.repository.RoomRepository;
import chat.hala.app.restful.wrapper.MemberInRoom;
import chat.hala.app.restful.wrapper.RoomAmount;
import chat.hala.app.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by astropete on 2018/1/29.
 */

@Service
public class RankServiceImplementation implements RankService {

    @Autowired
    private CoinTransactionRepository ctRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private RoomEnteringRepository reRepo;

    @Override
    public Object getGiftRanking(String type, Pageable pageable){
        Date start = General.date(type);
        List<Object[]> stats = ctRepo.findGiftReceiverMemberIdsInPage(start, pageable.getOffset(), pageable.getPageSize());
        return this.rankGeneral(stats);
    }

    @Override
    public Object getWealthRanking(String type, Pageable pageable){
        Date start = General.date(type);
        List<Object[]> stats = ctRepo.findGiftSenderMemberIdsInPage(start, pageable.getOffset(), pageable.getPageSize());
        return this.rankGeneral(stats);
    }

    @Override
    public Object getRoomRanking(String type, Pageable pageable){
        Date start = General.date(type);
        List<Object[]> stats = ctRepo.findWealthRoomIdsInPage(start, pageable.getOffset(), pageable.getPageSize());
        if(stats.size() == 0) return new ArrayList<>();
        List<Long> roomIds = new ArrayList<>();
        for(Object[] s : stats){
            roomIds.add(Long.valueOf(((String)s[1]).substring(4)));
        }
        List<Room> rs = roomRepo.findByIdListOrderByIdList(roomIds);
        int i = 0;
        List<RoomAmount> ras = new ArrayList<>();
        for(Room r : rs){
            RoomAmount ra = new RoomAmount();
            ra.setRoom(r);
            ra.setAmount(((BigDecimal)(stats.get(i)[0])).intValue());
            ras.add(ra);
            i+=1;
        }
        return ras;
    }

    @Override
    public Object getRoomMemberRanking(String type, Long roomId, Pageable pageable){
        Date start = General.date(type);
        List<Long> senderIds = ctRepo.findGiftSenderMemberIdsByRoomIdInPage("ROOM"+String.valueOf(roomId), start, pageable.getOffset(), pageable.getPageSize());
        List<Member> members = new ArrayList<>();
        List<MemberInRoom> mirs = new ArrayList<>();
        if(senderIds.size() > 0) members = memberRepo.findAllByIdListOrderByIdList(senderIds);
        for(Member member : members){
            member.discardSensitive();
            MemberInRoom mir = new MemberInRoom();
            mir.setMember(member);
            mir.setSpent(ctRepo.findGiftSenderSpentByRoomIdAndMemberId("ROOM"+String.valueOf(roomId), start, member.getId()));
            RoomEntering e = reRepo.findLastMemberEnterOrExit(member.getId(), roomId);
            mir.setInout(e == null ? false: e.getInout());
            mirs.add(mir);
        }
        Integer total = ctRepo.findGiftTotalValueByRoomAndDate("ROOM"+String.valueOf(roomId), start);
        Map<String, Object> re = new HashMap<>();
        re.put("total", total);
        re.put("member", mirs);
        return re;
    }

    private Object rankGeneral(List<Object[]> stats){
        if(stats.size() == 0) return new ArrayList<>();
        List<Long> memberIds = new ArrayList<>();
        for(Object[] s : stats) memberIds.add(((BigInteger)(s[1])).longValue());
        List<Member> members = new ArrayList<>();
        if(memberIds.size() > 0) members = memberRepo.findAllByIdListOrderByIdList(memberIds);
        for(Member member : members) member.discardSensitive();
        List<MemberAmount> mas = new ArrayList<>();
        int i = 0;
        for(Member m : members){
            MemberAmount ma = new MemberAmount();
            ma.setAmount(((BigDecimal)(stats.get(i)[0])).intValue());
            ma.setMember(m);
            mas.add(ma);
            i+=1;
        }
        return mas;
    }
}
