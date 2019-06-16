package chat.hala.app.service.implementation;

import chat.hala.app.entity.*;
import chat.hala.app.library.Identifier;
import chat.hala.app.library.RongCloud;
import chat.hala.app.library.util.Constant;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.General;
import chat.hala.app.library.util.Http;
import chat.hala.app.repository.*;
import chat.hala.app.restful.wrapper.MemberInRoom;
import chat.hala.app.restful.wrapper.MemberOnMic;
import chat.hala.app.restful.wrapper.MicrophoneSwitch;
import chat.hala.app.service.RoomService;
import chat.hala.app.service.exception.InvalidCredentialException;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.ObjectNotFoundException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import chat.hala.app.service.library.CoinTransactionRecorder;
import chat.hala.app.service.library.TaskHandler;
import com.google.common.base.Strings;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by astropete on 2018/1/28.
 */

@Service
public class RoomServiceImplementation implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private RoomEnteringRepository enterRepo;

    @Autowired
    private RoomMicrophoneHoldingRepository micRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private RoomRoleRepository roleRepo;

    @Autowired
    private CoinTransactionRecorder ctRecorder;

    @Autowired
    private RoomFollowerRepository rfRepo;

    @Autowired
    private RoomBanningRepository banRepo;

    @Autowired
    private CoinTransactionRepository ctRepo;

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private RoomStyleRepository rsRepo;

    @Override
    public Object createRoom(Member member, Room room, Coordinate locate, String language) throws PreconditionNotQualifiedException{
        List<Room> created = roomRepo.findByOwnerId(member.getId());
        if(created.size() > 0) throw new PreconditionNotQualifiedException(ErrorMsg.ONE_ROOM_ONLY);
       /* Map<Room.RoomStyle, Map> styles = Constant.getRoomStyle();
        Map<Map, Integer> styleParams = styles.get(room.getStyle());*/

        RoomStyle style = rsRepo.findOne(room.getStyleId());

        if(member.getCoins() < style.getCoin()/*styleParams.get("coin")*/) throw new PreconditionNotQualifiedException(ErrorMsg.NO_ENOUGH_COINS);
        GeometryFactory geoFactory = new GeometryFactory();
        if(Strings.isNullOrEmpty(room.getIntroduction())){
            if(language.equals(Constant.LAN_ARAB)) room.setIntroduction(Constant.LAN_ARAB_ROOM_INTRO);
            else room.setIntroduction(Constant.LAN_ENG_ROOM_INTRO);
        }
        Identifier idg = new Identifier(7, "", true);
        room.setCharacterId(idg.generate());
        room.setLocate(locate == null ? null : geoFactory.createPoint(locate));
        room.setPlaceName(member.getHometown());
        room.setHotweight(0);
        room.setActive(true);

        room.setWealth(0);
        room.setAttendersCount(1);
        room.setInsidersCount(1);
        room.setFansCount(0);
        room.setMicrophoneFacing(Constant.getDefaultMicrophoneFacing());
        room.setAdminLimit(style.getAdmin()/*styleParams.get("admin")*/);
        room.setAttenderLimit(style.getAttender()/*styleParams.get("attender")*/);
        room.setInsiderPrice(Constant.getDefaultInsiderPrice());
        room.setMicrophonePrice(Constant.getDefaultMicrophonePrice());
        room.setOwnerId(member.getId());
        if(room.getAvatarUrl() == null) room.setAvatarUrl(member.getAvatarUrl());
        room.setMicrophoneLimit(Constant.getDefaultMicrophoneCount());
        room.setMicrophoneSwitched(this.generateMicSwitch(room, null));
        room.setCreatedAt(new Date());
        room.setUpdatedAt(new Date());
        roomRepo.saveAndFlush(room);
        /*if(!room.getStyle().equals(Room.RoomStyle.standard))*/
        if (room.getStyleId()!=1) ctRecorder.recordRoomSpent(member.getId(), room, CoinTransaction.FromAction.addroom);
        RoomRole role = new RoomRole();
        role.setMemberId(member.getId());
        role.setRoomId(room.getId());
        role.setRole(RoomRole.MiR.owner);
        role.setCreatedAt(new Date());
        role.setUpdatedAt(new Date());
        roleRepo.saveAndFlush(role);
        RoomEntering enter = new RoomEntering();
        enter.setRoomId(room.getId());
        enter.setInout(true);
        enter.setMemberId(member.getId());
        enter.setCreatedAt(new Date());
        enterRepo.saveAndFlush(enter);
        member.setSpent(member.getSpent() + style.getCoin()/*styleParams.get("coin")*/);
        member.setCoins(member.getCoins() -style.getCoin() /*styleParams.get("coin")*/);
        memberRepo.saveAndFlush(member);
        taskHandler.completeTask(member, "addroom");
        return Http.coinResponse(room, member.getCoins());
    }

    @Override
    public Room getRoomById(Member member, Long roomId){
        Room room = roomRepo.findOne(roomId);
        if(room == null || !room.getActive()) return null;
        RoomFollower rf = rfRepo.findByRoomIdAndMemberId(roomId, member.getId());
        room.setFollow(rf == null ? false : rf.getActive());
        RoomRole rr = roleRepo.findByRoomIdAndMemberId(roomId, member.getId());
        room.setRole(rr == null ? RoomRole.MiR.undefined : rr.getRole());
        return room;
    }

    @Override
    public void enterOrLeaveRoom(Member member, Long roomId, RoomEntering entering) throws Exception, PreconditionNotQualifiedException{
        RoomEntering last = enterRepo.findLastMemberEnterOrExit(member.getId(), roomId);
        if(last != null && last.getInout().equals(entering.getInout())) return;
        Room r = roomRepo.findOne(roomId);
        if(!r.getActive()) throw new PreconditionNotQualifiedException(ErrorMsg.ROOM_INACTIVE);
        if(!member.getId().equals(r.getOwnerId()) && r.getAttendersCount() >= r.getAttenderLimit()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_SPARE_ROOM);
        Integer attendersCount = r.getAttendersCount();
        attendersCount += entering.getInout() ? 1 : -1;
        r.setAttendersCount(attendersCount);
        r.setUpdatedAt(new Date());
        roomRepo.saveAndFlush(r);
        entering.setMemberId(member.getId());
        entering.setRoomId(roomId);
        entering.setCreatedAt(new Date());
        entering.setUpdatedAt(new Date());
        if(!entering.getInout()) {
            RoomEntering lastEntering = enterRepo.findLastMemberEnter(member.getId(), roomId);
            if (((entering.getCreatedAt().getTime() - lastEntering.getCreatedAt().getTime()) / (60 * 1000) % 60) > 10)
                taskHandler.completeTask(member, "inroom10");
        }
        enterRepo.saveAndFlush(entering);
//        RongCloud rc = new RongCloud();
//        rc.sendInOutNotice(member, roomId, entering.getInout());
    }

    @Override
    public void switchMicrophone(Member member, Long roomId, MicrophoneSwitch switching) throws InvalidCredentialException, PreconditionNotQualifiedException{
        Room r = roomRepo.findOne(roomId);
        if(!r.getActive()) throw new PreconditionNotQualifiedException(ErrorMsg.ROOM_INACTIVE);
        if(!r.getOwnerId().equals(member.getId())) throw new InvalidCredentialException(ErrorMsg.NO_PERMISSION);
        if(r.getMicrophoneLimit() < switching.getMicrophoneNumber()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_SUCH_MIC);
        List<Integer> switchedOnMic = r.getSwitchedMicNumbersList();
        if((switchedOnMic.contains(switching.getMicrophoneNumber()) && switching.getSwitching()) || (!switching.getSwitching() && !switchedOnMic.contains(switching.getMicrophoneNumber()))) return;
        r.setMicrophoneSwitched(this.generateMicSwitch(r, switching.getMicrophoneNumber()));
        r.setUpdatedAt(new Date());
        roomRepo.saveAndFlush(r);
    }

    @Override
    public Object onOrOffMicrophone(Member holder, Long roomId, RoomMicrophoneHolding holding) throws ObjectAlreadyExistedException, PreconditionNotQualifiedException {
        if(!holding.getOnoff()){
            RoomMicrophoneHolding m = micRepo.findTop1ByRoomIdAndMemberIdOrderByCreatedAtDesc(roomId, holder.getId());
            m.setOnoff(false);
            m.setUpdatedAt(new Date());
            micRepo.saveAndFlush(m);
            if(((m.getUpdatedAt().getTime() - m.getCreatedAt().getTime()) / (60 * 1000) % 60) > 10) taskHandler.completeTask(holder, "mic10");
            return Http.generalResponse();
        }else{
            Room r = roomRepo.findOne(roomId);
            if(!r.getActive()) throw new PreconditionNotQualifiedException(ErrorMsg.ROOM_INACTIVE);
            if(r.getMicrophoneFacing().equals(Room.MicFacing.insider) && roleRepo.findByRoomIdAndMemberId(r.getId(), holder.getId()) == null) throw new PreconditionNotQualifiedException(ErrorMsg.MIC_FACING_DIFF);
            if(holder.getCoins() < r.getMicrophonePrice()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_ENOUGH_COINS);
            List<RoomMicrophoneHolding> mics = micRepo.findMicrophoneHoldings(roomId);
            if(mics.size() == r.getMicrophoneLimit()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_SPARE_MIC);
            for(RoomMicrophoneHolding mh : mics){
                if(mh.getMicrophoneNumber().equals(holding.getMicrophoneNumber())) throw new PreconditionNotQualifiedException(ErrorMsg.MIC_NUMBER_OCCUPIED);
                if(mh.getMemberId().equals(holder.getId())) throw new ObjectAlreadyExistedException(ErrorMsg.ALREADY_ON_MIC);
            }
            List<Integer> switchedOnMic = r.getSwitchedMicNumbersList();
            if(!switchedOnMic.contains(holding.getMicrophoneNumber())) throw new PreconditionNotQualifiedException(ErrorMsg.MIC_LOCKED);
            if(r.getMicrophoneLimit() < holding.getMicrophoneNumber()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_SUCH_MIC);
            holding.setMemberId(holder.getId());
            holding.setRoomId(roomId);
            holding.setCreatedAt(new Date());
            holding.setUpdatedAt(new Date());
            micRepo.saveAndFlush(holding);
            if(r.getMicrophonePrice() > 0) ctRecorder.recordRoomSpent(holder.getId(), r, CoinTransaction.FromAction.onmic);
            holder.setCoins(holder.getCoins() - r.getMicrophonePrice());
            holder.setSpent(holder.getSpent() + r.getMicrophonePrice());
            memberRepo.saveAndFlush(holder);
            r.setWealth(r.getWealth() + r.getMicrophonePrice());
            roomRepo.saveAndFlush(r);
            return Http.coinResponse(null, holder.getCoins());
        }
    }

    @Override
    public void addOrKickAAdmin(Member member, RoomRole role, Boolean grant) throws InvalidCredentialException, PreconditionNotQualifiedException{
        Room r = roomRepo.findOne(role.getRoomId());
        if(!r.getActive()) throw new PreconditionNotQualifiedException(ErrorMsg.ROOM_INACTIVE);
        if(!r.getOwnerId().equals(member.getId())) throw new InvalidCredentialException(ErrorMsg.NO_PERMISSION);
        RoomRole existed = roleRepo.findByRoomIdAndMemberId(role.getRoomId(), role.getMemberId());
        if(existed == null) throw new PreconditionNotQualifiedException(ErrorMsg.ADMIN_FACING_DIFF);
        if(existed.getRole().equals(RoomRole.MiR.owner) || (existed.getRole().equals(RoomRole.MiR.administrator) && grant) || (existed.getRole().equals(RoomRole.MiR.insider) && !grant)) return;
        List<RoomRole> roles = roleRepo.findByRoomIdAndRole(r.getId(), RoomRole.MiR.administrator);
        if(grant && roles.size() >= r.getAdminLimit()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_SPARE_ADMIN);
        existed.setRole(grant ? RoomRole.MiR.administrator : RoomRole.MiR.insider);
        existed.setUpdatedAt(new Date());
        roleRepo.saveAndFlush(existed);
    }

    @Override
    public void banActingInRoom(Member member, Long roomId, RoomBanning banning) throws Exception, InvalidCredentialException{
        if(roleRepo.findByRoomIdAndMemberIdAndRoleIsNot(roomId, member.getId(), RoomRole.MiR.insider) == null) throw new InvalidCredentialException(ErrorMsg.NO_PERMISSION);
        Long referenceId = null;
        RongCloud rc = new RongCloud();
        Member banned = memberRepo.findOne(banning.getBanMemberId());
        if(banning.getActing().equals(RoomBanning.RoomBan.out)){
            RoomEntering last = enterRepo.findLastMemberEnterOrExit(banning.getBanMemberId(), roomId);
            if(last == null || !last.getInout()) return;
            RoomEntering e = new RoomEntering();
            e.setRoomId(roomId);
            e.setMemberId(banning.getBanMemberId());
            e.setInout(false);
            e.setCreatedAt(new Date());
            e.setUpdatedAt(new Date());
            enterRepo.saveAndFlush(e);
            rc.sendInOutNotice(banned, roomId, false);
            rc.sendRoomBlock(banned, roomId);
            referenceId = e.getId();
        }
        if(banning.getActing().equals(RoomBanning.RoomBan.offmic)){
            RoomMicrophoneHolding m = micRepo.findTop1ByRoomIdAndMemberIdOrderByCreatedAtDesc(roomId, banning.getBanMemberId());
            if(m == null) return;
            m.setOnoff(false);
            m.setUpdatedAt(new Date());
            micRepo.saveAndFlush(m);
            referenceId = m.getId();
        }
        if(banning.getActing().equals(RoomBanning.RoomBan.silence)){
            rc.sendRoomSilence(banned, roomId);
        }
        banning.setReferenceId(referenceId);
        banning.setRoomId(roomId);
        banning.setMemberId(member.getId());
        banning.setCreatedAt(new Date());
        banning.setUpdatedAt(new Date());
        banRepo.saveAndFlush(banning);
    }

    @Override
    public Object upgradeARoom(Member member, Room room, Long roomId) throws InvalidCredentialException, PreconditionNotQualifiedException{
        Room existed = roomRepo.findOne(roomId);
        if(!existed.getActive()) throw new PreconditionNotQualifiedException(ErrorMsg.ROOM_INACTIVE);
        if(!member.getId().equals(existed.getOwnerId())) throw new InvalidCredentialException(ErrorMsg.NO_PERMISSION);
       /* Map<Room.RoomStyle, Map> styles = Constant.getRoomStyle();
        Map<Map, Integer> styleParams = styles.get(room.getStyle());
        if(styleParams.get("coin") > member.getCoins()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_ENOUGH_COINS);*/

        RoomStyle style= rsRepo.findOne(room.getStyleId());
        if (style.getCoin()>member.getCoins()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_ENOUGH_COINS);

        existed.setStyleId(style.getId());
        existed.setAdminLimit(style.getAdmin());
        existed.setAttenderLimit(style.getAttender());
       /* existed.setStyle(room.getStyle());
        existed.setAdminLimit(styleParams.get("admin"));
        existed.setAttenderLimit(styleParams.get("attender"));*/
        existed.setUpdatedAt(new Date());
        roomRepo.saveAndFlush(existed);
        ctRecorder.recordRoomSpent(member.getId(), existed, CoinTransaction.FromAction.upgraderoom);
        member.setSpent(member.getSpent()+style.getCoin());
        member.setCoins(member.getCoins()-style.getCoin());
        /* member.setSpent(member.getSpent() + styleParams.get("coin"));
        member.setCoins(member.getCoins() - styleParams.get("coin"));*/
        memberRepo.saveAndFlush(member);
        return Http.coinResponse(existed, member.getCoins());
    }

    @Override
    public void updateARoom(Member member, Room room, Long roomId) throws InvalidCredentialException{
        Room existed = roomRepo.findOne(roomId);
        if(!existed.getActive()) return;
        if(!member.getId().equals(existed.getOwnerId())) throw new InvalidCredentialException(ErrorMsg.NO_PERMISSION);
        if(room.getMicrophoneLimit() != null) existed.setMicrophoneLimit(room.getMicrophoneLimit());
        if(room.getName() != null) existed.setName(room.getName());
        if(room.getAvatarUrl() != null) existed.setAvatarUrl(room.getAvatarUrl());
        if(room.getIntroduction() != null) existed.setIntroduction(room.getIntroduction());
        if(room.getMicrophonePrice() != null) existed.setMicrophonePrice(room.getMicrophonePrice());
        if(room.getInsiderPrice() != null) existed.setInsiderPrice(room.getInsiderPrice());
        if(room.getMicrophoneFacing() != null) existed.setMicrophoneFacing(room.getMicrophoneFacing());
        roomRepo.saveAndFlush(existed);
    }

    @Override
    public void removeARoom(Member member, Long roomId) throws InvalidCredentialException{
        Room r = roomRepo.findOne(roomId);
        if(!member.getId().equals(r.getOwnerId())) throw new InvalidCredentialException(ErrorMsg.NO_PERMISSION);
        roomRepo.delete(r);
    }

    @Override
    public List<MemberOnMic> getRoomMics(Long roomId) throws ObjectNotFoundException{
        List<RoomMicrophoneHolding> holdings = micRepo.findMicrophoneHoldings(roomId);
        Room r = roomRepo.findOne(roomId);
        if(r == null) throw new ObjectNotFoundException(ErrorMsg.NO_ROOM);
        if(Strings.isNullOrEmpty(r.getMicrophoneSwitched())) return new ArrayList<>();
        List<Integer> micNumbers = r.getStandardMicNumbersList();
        List<Integer> switchedMicNumbers = r.getSwitchedMicNumbersList();
        List<MemberOnMic> re = new ArrayList<>();
        for(Integer i : micNumbers){
            MemberOnMic mom = new MemberOnMic();
            mom.setMicrophoneNumber(i);
            mom.setSwitching(switchedMicNumbers.contains(i));
            mom.setMember(null);
            for(RoomMicrophoneHolding h : holdings){
                if(h.getMicrophoneNumber().equals(i)){
                    Member m = memberRepo.findOne(h.getMemberId());
                    m.discardSensitive();
                    mom.setMember(m);
                }
            }
            re.add(mom);
        }
        return re;
    }

    @Override
    public List<MemberInRoom> getRoomAttenders(Long roomId, String excludes, Pageable pageable){
        List<RoomEntering> enterings = enterRepo.findByRoomIdOrderByCreatedAtAsc(roomId);
        List<Long> inRoomMemberIds = new ArrayList<>();
        for(RoomEntering enter : enterings){
            if(enter.getInout()) inRoomMemberIds.add(enter.getMemberId());
            else inRoomMemberIds.remove(enter.getMemberId());
        }
        if(excludes != null){
            final List<Long> excludeIds = Arrays.stream(excludes.split(",")).map(Long::parseLong).collect(Collectors.toList());
            inRoomMemberIds.removeAll(excludeIds);
        }
        if(inRoomMemberIds.size() == 0) return new ArrayList<>();
        List<Member> members = memberRepo.findByIdListOrderByIdListInPage(inRoomMemberIds, pageable.getOffset(), pageable.getPageSize());
        List<MemberInRoom> mirs = new ArrayList<>();
        for(Member m : members){
            m.discardSensitive();
            MemberInRoom mir = new MemberInRoom();
            mir.setMember(m);
            mir.setSpent(ctRepo.findGiftSenderSpentByRoomIdAndMemberId("ROOM"+String.valueOf(roomId), General.date("daily"), m.getId()));
            mir.setInout(true);
            mirs.add(mir);
        }
        return mirs;
    }

    @Override
    public List<Member> getRoomMembers(Long roomId, Pageable pageable){
        List<RoomRole> roles = roleRepo.findByRoomIdOrderByRoleAndTimeAsc(roomId);
        List<Long> memberIds = new ArrayList<>();
        for(RoomRole role : roles) memberIds.add(role.getMemberId());
        List<Member> re = memberRepo.findByIdListOrderByIdListInPage(memberIds, pageable.getOffset(), pageable.getPageSize());
        for(Member m : re) m.discardSensitive();
        return re;
    }

    @Override
    public List<Long> getRoomAdminIds(Long roomId){
        List<RoomRole> roles = roleRepo.findByRoomIdAndRoleIsNot(roomId, RoomRole.MiR.insider);
        List<Long> memberIds = new ArrayList<>();
        for(RoomRole role : roles) memberIds.add(role.getMemberId());
        return memberIds;
    }

    @Override
    public List<Room> getLatestRooms(Member member, Pageable pageable){
        List<Room> rl = roomRepo.findByLatestAndIsActiveTrue(pageable.getOffset(), pageable.getPageSize(), member.getId());
        return rl;
    }

    @Override
    public Page<Room> getFollowedRooms(Member member, Pageable pageable){
        List<RoomFollower> followings = rfRepo.findByMemberIdAndIsActiveTrue(member.getId());
        List<Long> roomIds = new ArrayList<>();
        for(RoomFollower rf : followings) roomIds.add(rf.getRoomId());
        return roomRepo.findByIdInAndIsActiveTrueAndOwnerIdIsNot(roomIds, member.getId(), pageable);
    }

    @Override
    public List<Room> getNearbyRooms(Member member, Double longitude, Double latitude, Integer page, Integer size){
        return roomRepo.findNearby(longitude, latitude, page * size, size, member.getId());
    }

    @Override
    public List<Room> getExploreRooms(String country, Integer page, Integer size){
        Room attendersMost = roomRepo.findMostAttendersRoom();
        Room wealthMost = roomRepo.findMostWealthRoom();
        Integer maxAttender = attendersMost == null ? 1 : attendersMost.getAttendersCount();
        Integer maxWealth = wealthMost == null ? 1 : wealthMost.getWealth();
        if(country == null){
            return roomRepo.findExplore(maxAttender, maxWealth, page * size, size);
        }else{
            String like = "%" + country + "%";
            return roomRepo.findExploreInCountry(country, maxAttender, maxWealth, page * size, size);
        }
    }

    @Override
    public Object getRoomsByMember(Member member, Long requestMemberId){
        Map<String, List> re = new HashMap<>();
        List<Long> followRoomIds = rfRepo.findRoomIdByMemberId(requestMemberId);
        if(followRoomIds.size() == 0) re.put("followed", new ArrayList());
        else re.put("followed", roomRepo.findByIdInOrderByWealthDesc(followRoomIds, member.getId()));
        List<RoomRole> rrs = roleRepo.findByMemberId(requestMemberId);
        List<Long> ownedRoomIds = new ArrayList<>();
        List<Long> joinedRoomIds = new ArrayList<>();
        for(RoomRole r : rrs){
            if(r.getRole().equals(RoomRole.MiR.owner)) ownedRoomIds.add(r.getRoomId());
            else joinedRoomIds.add(r.getRoomId());
        }
        if(ownedRoomIds.size() == 0) re.put("owned", new ArrayList());
        else re.put("owned", roomRepo.findByIdInOrderByWealthDesc(ownedRoomIds, 0L));
        if(joinedRoomIds.size() == 0) re.put("joined", new ArrayList());
        else re.put("joined", roomRepo.findByIdInOrderByWealthDesc(joinedRoomIds, member.getId()));
        return re;
    }

    @Override
    public Page<Room> searchRoomByKeyword(String keyword, Pageable pageable){
        String like = "%" + keyword + "%";
        return roomRepo.findByNameLikeAndIsActiveTrue(like, pageable);
    }

    @Override
    public Object becomeARoomInsider(Member member, Long roomId) throws PreconditionNotQualifiedException {
        Room room = roomRepo.findOne(roomId);
        if(!room.getActive()) throw new PreconditionNotQualifiedException(ErrorMsg.ROOM_INACTIVE);
        if(member.getCoins() < room.getInsiderPrice()) throw new PreconditionNotQualifiedException(ErrorMsg.NO_ENOUGH_COINS);
        RoomRole existed = roleRepo.findByRoomIdAndMemberId(roomId, member.getId());
        if(existed != null) return Http.generalResponse();
        RoomRole role = new RoomRole();
        role.setMemberId(member.getId());
        role.setRoomId(roomId);
        role.setRole(RoomRole.MiR.insider);
        role.setCreatedAt(new Date());
        role.setUpdatedAt(new Date());
        roleRepo.saveAndFlush(role);
        if(room.getInsiderPrice() > 0) ctRecorder.recordRoomSpent(member.getId(), room, CoinTransaction.FromAction.insider);
        member.setCoins(member.getCoins() - room.getInsiderPrice());
        member.setSpent(member.getSpent() + room.getInsiderPrice());
        memberRepo.saveAndFlush(member);
        room.setWealth(room.getWealth() + room.getInsiderPrice());
        room.setInsidersCount(room.getInsidersCount() + 1);
        roomRepo.saveAndFlush(room);
        taskHandler.completeTask(member, "roommember");
        return Http.coinResponse(role, member.getCoins());
    }

    @Override
    public void revokeARoomInsider(Member member, Long roomId){
        RoomRole role = roleRepo.findByRoomIdAndMemberId(roomId, member.getId());
        if(role != null && !role.getRole().equals(RoomRole.MiR.owner)){
            roleRepo.delete(role);
            Room r = roomRepo.findOne(roomId);
            r.setInsidersCount(r.getInsidersCount() - 1);
            roomRepo.saveAndFlush(r);
        }
    }

    @Override
    public void followARoom(Member member, Long roomId){
        RoomFollower existed = rfRepo.findByRoomIdAndMemberId(roomId, member.getId());
        if(existed != null){
            if(existed.getActive()){
                return;
            }else{
                existed.setActive(true);
                existed.setUpdatedAt(new Date());
                rfRepo.saveAndFlush(existed);
            }
        }else{
            RoomFollower rf = new RoomFollower();
            rf.setMemberId(member.getId());
            rf.setRoomId(roomId);
            rf.setActive(true);
            rf.setCreatedAt(new Date());
            rf.setUpdatedAt(new Date());
            rfRepo.saveAndFlush(rf);
            taskHandler.completeTask(member, "likeroom");
        }
        Room room = roomRepo.findOne(roomId);
        room.setFansCount(room.getFansCount() + 1);
        roomRepo.saveAndFlush(room);
    }

    @Override
    public void unfollowARoom(Member member, Long roomId){
        RoomFollower existed = rfRepo.findByRoomIdAndMemberId(roomId, member.getId());
        if(existed == null) return;
        existed.setUpdatedAt(new Date());
        existed.setActive(false);
        rfRepo.saveAndFlush(existed);
        Room room = roomRepo.findOne(roomId);
        room.setFansCount(room.getFansCount() - 1);
        roomRepo.saveAndFlush(room);
    }

    private String generateMicSwitch(Room r, Integer switchIndex){
        List<Integer> rms = new ArrayList<>();
        if(r.getMicrophoneSwitched() == null){
            rms = r.getStandardMicNumbersList();
        }else{
            rms = r.getSwitchedMicNumbersList();
        }
        if(switchIndex == null) return rms.toString().substring(1, rms.toString().length() - 1).replace(" ", "");
        if(rms.contains(switchIndex)){
            rms.remove(switchIndex);
        }else{
            rms.add(switchIndex);
        }
        Collections.sort(rms);
        return rms.toString().substring(1, rms.toString().length() - 1).replace(" ", "");
    }

}
