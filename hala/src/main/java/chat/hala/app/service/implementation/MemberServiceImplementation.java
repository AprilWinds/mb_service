package chat.hala.app.service.implementation;

import chat.hala.app.entity.*;
import chat.hala.app.library.RongCloud;
import chat.hala.app.library.VerificationCode;
import chat.hala.app.library.util.*;
import chat.hala.app.repository.*;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.restful.wrapper.MemberStatus;
import chat.hala.app.service.MemberService;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by astropete on 2017/12/26.
 */

@Service
public class MemberServiceImplementation implements MemberService {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private MemberFondingRepository memberFondingRepo;

    @Autowired
    private FondingRepository fondingRepo;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private RelationshipRepository relationRepo;

    @Autowired
    private MemberSettingRepository settingRepo;

    @Autowired
    private MemberCheckinLogRepository checkinRepo;

    @Autowired
    private BlockingRepository blockRepo;

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Autowired
    private MemberTaskLogRepository mtlRepo;

    @Autowired
    private CoinTransactionRepository ctRepo;

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private CoinTransactionRecorder ctRecorder;

    @Autowired
    private RoomRepository rRepo;

    @Autowired
    private  TagRepository tRepo;

    @Autowired
    private DynamicRepository dRepo;

    @Autowired
    private StarRepository sRepo;


    @Autowired
    private SystemNoticeRepository snRepo;

    @Autowired
    private DriftBottleRepository dbRepo;

    @Override
    public void proposeFeedback(Member member, Feedback feedback){
        feedback.setMemberId(member.getId());
        feedback.setReplied(false);
        feedback.setCreatedAt(new Date());
        feedback.setUpdatedAt(new Date());
        feedbackRepo.saveAndFlush(feedback);
    }

    @Override
    public Member updateAMember(Member member, Member updated, String language){
        member.setNickname(updated.getNickname() != null ? updated.getNickname() : member.getNickname());
        member.setAvatarUrl(updated.getAvatarUrl() != null ? updated.getAvatarUrl() : member.getAvatarUrl());
        member.setDob(updated.getDob() != null ? updated.getDob() : member.getDob());
        member.setGender(updated.getGender() != null ? updated.getGender() : member.getGender());
        member.setIntroduction(updated.getIntroduction() != null ? updated.getIntroduction() : member.getIntroduction());
        member.setHometown(updated.getHometown() != null ? updated.getHometown() : member.getHometown());
        member.setAffectiveState(updated.getAffectiveState()!=null ?updated.getAffectiveState():member.getAffectiveState());
        member.setBackground(updated.getBackground()!=null?updated.getBackground():member.getBackground());
        member.setCurrentResidence(updated.getCurrentResidence()!=null?updated.getCurrentResidence():member.getCurrentResidence());
        member.setUpdatedAt(new Date());
        Member m = memberRepo.saveAndFlush(member);
        //  taskHandler.completeTask(member, "profile");
       /* if(member.getHometown() != null){
            List<Room> rs = rRepo.findByOwnerId(member.getId());
            for(Room r : rs){
                r.setPlaceName(member.getHometown());
                rRepo.saveAndFlush(r);
            }
        }*/
       tags(m,language);

       return m;

    }

    @Override
    public void setMemberPushToken(Member member, Member tokenM){
        member.setApToken(tokenM.getApToken());
        memberRepo.saveAndFlush(member);
    }

    @Override
    public Object getMemberRongToken(Member member) throws Exception{
        RongCloud rc = new RongCloud();
        String token = rc.getToken(member).toString();
        member.setRongToken(token);
        memberRepo.saveAndFlush(member);
        return Http.resultResponse(token);
    }

    @Override
    public Member getMemberById(Member requestedMember, Long memberId, String language) throws NotFoundException {
        Member m = memberRepo.findById(memberId);
        if(m == null) {throw new NotFoundException(ErrorCode.NO_MEMBER.getMsg());}
        m.setFollowingCount(relationRepo.findCountOfFollowing(memberId));
        m.setFanCount(relationRepo.findCountOfFan(memberId));
        m.setFriendsCount(relationRepo.findCountOfFriend(memberId));
        m.setHometown(m.getHometown());
        String age=null;
        try {
            age=General.getAgeByBirth(m.getDob());
        } catch (ParseException e) {
            age="";
        }
        m.setAge(age);

        m.setDynamicCount(dRepo.findDynamicCountById(memberId));
        if(!requestedMember.getId().equals(memberId)){
            m.discardSensitive();
            m.setRelation(this.getMemberRelation(requestedMember, m));
            m.setDistance(this.getMemberDistance(requestedMember, m));
        }else{
            m.setHasReward(false);
         /*   List<BigInteger> finishedDailyTaskIds = mtlRepo.findMemberCompletedDailyTaskIds(memberId);
            if(finishedDailyTaskIds.size() > 0){
                Integer rewardedDailyTaskCount = ctRepo.findCountOfDailyRewardedTask(finishedDailyTaskIds, memberId);
                if(finishedDailyTaskIds.size() > rewardedDailyTaskCount) m.setHasReward(true);
            }else{
                List<BigInteger> finishedNonDailyTaskIds = mtlRepo.findMemberCompletedNonDailyTaskIds(memberId);
                if(finishedNonDailyTaskIds.size() > 0){
                    Integer rewardedNonDailyTaskCount = ctRepo.findCountOfNonDailyRewardedTask(finishedNonDailyTaskIds, memberId);
                    if(finishedNonDailyTaskIds.size() > rewardedNonDailyTaskCount) m.setHasReward(true);
                }
            }*/
        }
        tags(m,language);
        for(Fonding f : m.getFondings()){
            if(language.equals(Constant.LAN_ARAB)){
                f.setName(f.getArabicName());
            }
            f.setArabicName(null);
        }

        return m;
    }





    @Override
    public Member getMemberByCharacterId(Member requestedMember, String characterId, String language) throws Exception{
        Member m = memberRepo.findByCharacterIdAndIsActiveTrue(characterId);
        if(m == null) return null;
        else return this.getMemberById(requestedMember, m.getId(), language);
    }

    @Override
    public Member bindAMember(Member member, String code, Member bind) throws ObjectAlreadyExistedException, PreconditionNotQualifiedException {
        if(memberRepo.findByMobileNumberAndIsActiveTrue(bind.getMobileNumber()) != null) throw new ObjectAlreadyExistedException(ErrorMsg.MEMBER_EXISTED);
        VerificationCode coder = new VerificationCode(bind.getMobileNumber());
        if(!coder.getCurrentCode().equals(code) && !coder.getLastPeriodCode().equals(code) && !code.equals(Constant.getTestCode())) throw new PreconditionNotQualifiedException(ErrorMsg.CODE_NOT_MATCH);
        member.setMobileNumber(bind.getMobileNumber());
        member.setPassword(Encryption.MD5ed(bind.getPassword()));
        member.setUpdatedAt(new Date());
        memberRepo.saveAndFlush(member);
        taskHandler.completeTask(member, "phone");
        return member;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Member addMemberFondings(Member member, String fondingIds){
        memberFondingRepo.deleteAllFondingByMemberId(member.getId());
        String[] fondingIdArr = fondingIds.split("\\|");
        for(String fondingId : fondingIdArr){
            Fonding fonding = fondingRepo.findById(Long.valueOf(fondingId));
            MemberFonding f = new MemberFonding();
            f.setMemberId(member.getId());
            f.setFondingId(fonding.getId());
            f.setCreatedAt(new Date());
            f.setUpdatedAt(new Date());
            memberFondingRepo.saveAndFlush(f);
            fonding.setUsedCount(fonding.getUsedCount() + 1);
            fondingRepo.saveAndFlush(fonding);
        }
        return memberRepo.findById(member.getId());
    }

    @Override
    public Page<Member> searchMembersByKeyword(String keyword, Pageable pageable){
        String sqlFormattedKeyword = "%" + keyword + "%";
        return memberRepo.findByCharacterIdLikeOrNicknameLike(sqlFormattedKeyword, sqlFormattedKeyword, pageable);
    }

    @Override
    public void reportMember(Member member, Report r){
        r.setReporterId(member.getId());
        r.setCreatedAt(new Date());
        r.setUpdatedAt(new Date());
        reportRepo.saveAndFlush(r);
    }

    @Override
    public void updateMemberLocate(Member member, float longitude, float latitude){
        Coordinate c = new Coordinate(longitude, latitude);
        GeometryFactory geoFactory = new GeometryFactory();
        member.setLocate(geoFactory.createPoint(c));
        member.setLastActive(new Date());
        memberRepo.saveAndFlush(member);
    }

    @Override
    public Object updateMemberSetting(Member member, MemberSetting setting){
        MemberSetting s = settingRepo.findByMemberId(member.getId());
        if(s == null) s = this.generateDefaultSetting(member);
        if(setting.getLive() != null) s.setLive(setting.getLive());
        if(setting.getDistance() != null) s.setDistance(setting.getDistance());
        if(setting.getPushItems() != null){
            String push = (setting.getPushItems().get("greeting") ? "1" : "0") + (setting.getPushItems().get("room") ? "1" : "0") + (setting.getPushItems().get("online") ? "1" : "0");
            s.setPush(Integer.parseInt(push, 2));
        }
        return settingRepo.saveAndFlush(s);
    }

    @Override
    public Object getMemberSetting(Member member){
        MemberSetting ms = settingRepo.findByMemberId(member.getId());
        if(ms == null) return this.generateDefaultSetting(member);
        else return ms;
    }

    @Override
    public Object getCheckinStatus(Member member){
        MemberCheckinLog log = checkinRepo.findYesterdayCheckin(member.getId());
        Map<String, Object> re = new HashMap<>();
        int today = (log == null ? 0 : log.getContinuousCount()) + 1;
        re.put("today", today % 7);
        re.put("sequence", Constant.getCheckinReward());
        return re;
    }

    @Override
    public void checkin(Member member) throws ObjectAlreadyExistedException{
        if(checkinRepo.findTodayCheckin(member.getId()) != null) throw new ObjectAlreadyExistedException(ErrorMsg.CHECK_IN_ALREADY);
        MemberCheckinLog yesterdayLog = checkinRepo.findYesterdayCheckin(member.getId());
        int today = (yesterdayLog == null ? 0 : yesterdayLog.getContinuousCount()) + 1;
        MemberCheckinLog log = new MemberCheckinLog();
        log.setContinuousCount(today);
        log.setCreatedAt(new Date());
        log.setMemberId(member.getId());
        checkinRepo.saveAndFlush(log);
        ctRecorder.recordCheckInIncome(member.getId(), Constant.getCheckinReward()[(today-1)%7], today);
        member.setCoins(member.getCoins() + Constant.getCheckinReward()[(today-1)%7]);
        memberRepo.saveAndFlush(member);
    }

    @Override
    public void logout(Member member){
        member.setOnline(false);
        member.setApToken("");
        memberRepo.saveAndFlush(member);
    }

    public Member.RelationType getMemberRelation(Member a, Member b){
        List<Relationship> rl = relationRepo.findFollowingRelation(a.getId(), b.getId());
        if(rl.size() > 0){
            if(rl.size() == 2) return Member.RelationType.friend;
            if(rl.get(0).getFollowerId() == a.getId()) return Member.RelationType.following;
            return Member.RelationType.followed;
        }
        List<Blocking> bl = blockRepo.findBlockingRelation(a.getId(), b.getId());
        if(bl.size() > 0){
            if(bl.size() == 2) return Member.RelationType.hater;
            if(bl.get(0).getBlockerId() == a.getId()) return Member.RelationType.blocking;
            return Member.RelationType.blocked;
        }
        return Member.RelationType.none;
    }

    private Integer getMemberDistance(Member a, Member b){
        if(a.getLocate() == null || b.getLocate() == null) return null;
        Double d = General.distance(a.getLocate().getY(), b.getLocate().getY(), a.getLocate().getX(), b.getLocate().getX(), 0.0f, 0.0f);
        return d.intValue();
    }

    private MemberSetting generateDefaultSetting(Member m){
        MemberSetting ms = new MemberSetting();
        ms.setMemberId(m.getId());
        ms.setLive(true);
        ms.setDistance(true);
        ms.setPush(7);
        ms.setCreatedAt(new Date());
        ms.setUpdatedAt(new Date());
        return settingRepo.saveAndFlush(ms);
    }

    @Transactional
    @Override
    public Object
    findNearbyPeople(Long id, Double lng, Double lat,  Integer page, Integer size,String flag) {
        List<Object[]> people = memberRepo.findNearbyPeople(id, lng, lat, page * size, size,flag);
        stringUpdateMemberLocate(lng,lat,id);
        List<Map<String, Object>> ls = nearbyMemberInfo(people);
        Map<String, Object> map = new HashMap<>();
        map.put("people", ls);
        if (page == 0) {
            List<String> avatar = memberRepo.findOnlineAvatar(id);
            map.put("avatar",avatar);
        }
        return Http.standardResponse(map,200, "success");
    }
    @Transactional
    @Override
    public Object findOnlinePeople(Long id, Double lng, Double lat, Integer page, Integer size) {
        List<Object[]> people = memberRepo.findOnlinePeople(id, lng, lat, page * size, size);
        stringUpdateMemberLocate(lng,lat,id);
        List<Map<String, Object>> ls = nearbyMemberInfo(people);
        return Http.standardResponse(ls,200, "success");
    }

    @Transactional
    @Override
    public Object cloudUpdateStatus(List<MemberStatus> ls) {
        ls.forEach(x->{
            memberRepo.cloudUpdateStatus(x.getUserid(),x.getStatus().equals("0")?1:0,new Date());
        });
        return  Http.generalResponse();
    }

    @Override
    public Object getTags( String language) {
        List<Tag> all = tRepo.findAll();
        Map<String,List<Tag>> map=new HashMap<>();
        all.forEach(one->{
            if (one!=null){
                List<Tag> ls=new ArrayList<>();
                if (language.equals(Constant.LAN_ARAB)){
                    one.setCategory(one.getArabicCategory());
                    one.setTagName(one.getArabicTagName());
                    if (map.containsKey(one.getArabicCategory())){
                        List<Tag> temp = map.get(one.getArabicCategory());
                        temp.add(one);
                        map.put(one.getArabicCategory(),temp);
                    }else{
                        ls.add(one);
                        map.put(one.getArabicCategory(),ls);
                    }
                }else{
                    if (map.containsKey(one.getCategory())){
                        List<Tag> temp = map.get(one.getCategory());
                        temp.add(one);
                        map.put(one.getCategory(),temp);
                    }else {
                        ls.add(one);
                        map.put(one.getCategory(), ls);

                    }
                }
            }
        });

        List<Category> ls=new ArrayList<>();
        Set<String> set = map.keySet();
        set.forEach(x->{
            Category category=new Category();
            category.setName(x);
            category.setLs(map.get(x));
            ls.add(category);
        });

        return  Http.standardResponse(ls,200, "success");
    }


    @Override
    public Object updateTag(Member member, String arg) {
        if (!Strings.isNullOrEmpty(arg)){
            member.setTagId(arg);
            memberRepo.saveAndFlush(member);
        }
        return  Http.standardResponse();
    }

    @Override
    public Object addTag(Member member, String arg) {
        String tagId = member.getTagId();
        if (Strings.isNullOrEmpty(tagId)){member.setTagId(arg);}
        else{member.setTagId(tagId+","+arg);}
        memberRepo.saveAndFlush(member);
        return Http.standardResponse(null,200, "success");
    }

    @Override
    public Object deleteTag(Member member, String arg) {
        String[] split = arg.split(",");
        String tagId = member.getTagId();
        if (tagId.equals("")){
            return  Http.standardResponse(null,200,"success");
        }
        String[] str = tagId.split(",");
        StringBuilder sb=new StringBuilder();
        for (String s : str){
            if(Arrays.binarySearch(split,s)<0){
                sb.append(s + ",");
            }
        }
        String td=sb.toString();
        if(td.length()==1){member.setTagId("");return Http.standardResponse(200,"success");}
        member.setTagId(td.substring(0,td.length()-1));
        memberRepo.saveAndFlush(member);
        return Http.standardResponse(200, "success");
    }

    @Override
    public Object findDynamics(Member member, Long memberId, Pageable pageable) throws NotFoundException {
        Integer page=pageable.getOffset();
        Integer size=pageable.getPageSize();
        List<Dynamic> ls=new ArrayList<>();
        if (member.getId().longValue()==memberId) {
            ls = dRepo.findByMemberIdAndTypePage(member.getId(), Dynamic.Type.invisible.getState(), page,size);
            ls.forEach(x -> {
                x.setIsPraise(sRepo.findMemberStarState(x.getId(),memberId));
                DynamicServiceImplementation.memberInfo(x,member);
            });
            return  Http.standardResponse(ls);
        }else{
            Member one = memberRepo.findOne(memberId);
            if (one==null){throw new NotFoundException(ErrorMsg.NO_MEMBER); }
            Member.RelationType memberRelation = this.getMemberRelation(member, one);
            if (memberRelation==Member.RelationType.friend){
                 ls=dRepo.findByMemberIdAndTypePage(memberId, Dynamic.Type.me.getState(),page,size);
            }else if (memberRelation==Member.RelationType.followed){
                ls=dRepo.findByMemberIdAndTypePage(memberId,Dynamic.Type.friend.getState(),page,size);
            }else if( (memberRelation==Member.RelationType.none)||(memberRelation==Member.RelationType.following)){
                 ls=dRepo.findByMemberIdAndTypePage(memberId, Dynamic.Type.follow.getState(),page,size);
            }else{
                return  Http.standardResponse(ErrorCode.BLOKED);
            }
            ls.forEach(x->{
                x.setIsPraise(sRepo.findMemberStarState(x.getId(),memberId));
                x.setRelation(memberRelation);
                DynamicServiceImplementation.memberInfo(x,one);
            });
            return  Http.standardResponse(ls);
        }
    }


    @Override
    public Object getSystemNotice(Member member, Pageable pageable) {
        List<SystemNotice> ls= snRepo.findNoticeList(member.getId(),pageable.getOffset(),pageable.getPageSize());
        ls.forEach(x->{
            Member m = memberRepo.findOne(x.getMemberId());
            x.setMsg(m.getNickname()+x.getMsg());
            x.setAvatarUrl(m.getAvatarUrl());
            x.setNickname(m.getNickname());
            x.setGender(m.getGender());
            try {
                x.setAge(General.getAgeByBirth(m.getDob()));
            } catch (ParseException e) {
                x.setAge("");
            }
        });
        return Http.standardResponse(ls);
    }

    @Transactional
    @Override
    public Object readNoticeList(Long noticeId, Long memberId) {
        SystemNotice notice = snRepo.findOne(noticeId);
        snRepo.readNoticeList(memberId,notice.getTime());
        return Http.standardResponse();
    }

    @Override
    public Object createBottle(String content, Member member) {
        DriftBottle bottle=new DriftBottle();
        bottle.setContent(content);
        bottle.setTime(new Date());
        bottle.setSenderId(member.getId());
        dbRepo.saveAndFlush(bottle);
        member.setSendCount(member.getSendCount()-1);
        memberRepo.saveAndFlush(member);
        return Http.standardResponse();
    }


    @Override
    public Object getBottle(Member member) {
        DriftBottle bottle=dbRepo.getNotMeBottle(member.getId());
        Long senderId = bottle.getSenderId();
        Member res=memberRepo.getOne(senderId);
        bottle.setGender(res.getGender());
        bottle.setAvatarUrl(res.getBottleAvatar());
        member.setSalvageCount(member.getSalvageCount()-1);
        memberRepo.saveAndFlush(member);
        return Http.standardResponse(bottle);
    }

    class Category{
        private  String name;
        private  List<Tag> ls;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Tag> getLs() {
            return ls;
        }

        public void setLs(List<Tag> ls) {
            this.ls = ls;
        }
    }

    void tags(Member m,String language){
        String tagId = m.getTagId();
        if (Strings.isNullOrEmpty(tagId)) {m.setTags(new HashSet<>());}
        else{ String[] split = tagId.split(",");
            Set<String> set=new HashSet<>();
            for (String s:split){
                if(!Strings.isNullOrEmpty(s)){
                    Tag one = tRepo.findOne(Long.valueOf(s));
                    if (one!=null){
                        if (language.equals(Constant.LAN_ARAB)){
                            set.add(one.getArabicTagName());
                        }else{set.add(one.getTagName());}
                    }
                }
            } m.setTags(set);
        }
    }

    static List<Map<String, Object>> nearbyMemberInfo(List<Object[]> people){
        List<Map<String, Object>> ls=new ArrayList<>();
        if (people!=null&&people.size()!=0) {
            ls = new ArrayList<>();
            for (Object[] x : people) {
                Map<String, Object> map = new HashMap<>();
                map.put("nickname", String.valueOf(x[0]));
                map.put("avatarUrl", String.valueOf(x[1]));
                Boolean i = (Boolean) x[2];
                map.put("gender", i ? "female" : "male");
                Timestamp timestamp = (Timestamp) x[3];
                map.put("lastActive", String.valueOf(timestamp.getTime()));
                map.put("distance",x[5]);
                BigInteger age = (BigInteger) x[4];
                map.put("age", age == null ? "" : age.toString());
                map.put("introduction",x[6]==null? "":x[6]);
                map.put("id",x[7]);
                map.put("characterId",x[8]);
                ls.add(map);
            }
        }
        return ls;
    }

   public  void  stringUpdateMemberLocate(Double lng,Double lat,Long id){
        StringBuilder sb=new StringBuilder("POINT(");
        StringBuilder locate = sb.append(lng.toString()).append(" ").append(lat.toString()).append(")");
        memberRepo.triggerUpdateLocate(locate.toString(), id);
    }
}



