package chat.hala.app.service.implementation;

import chat.hala.app.entity.*;
import chat.hala.app.library.RongCloud;
import chat.hala.app.library.util.*;
import chat.hala.app.repository.*;
import chat.hala.app.service.WAService;
import chat.hala.app.service.exception.ObjectNotFoundException;
import chat.hala.app.service.library.CoinTransactionRecorder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by astropete on 2018/6/12.
 */

@Service
public class WAServiceImplementation implements WAService {

    @Autowired
    private MemberRepository mRepo;

    @Autowired
    private RoomRepository rRepo;

    @Autowired
    private FeedbackRepository fbRepo;

    @Autowired
    private AppMaterialRepository amRepo;

    @Autowired
    private ReportRepository reRepo;

    @Autowired
    private WebAdminRepository waRepo;

    @Autowired
    private RechargeRepository rgRepo;

    @Autowired
    private CoinTransactionRecorder ctr;


    @Autowired
    private ApprovalRepository aRepo;

    @Autowired
    private RoomBackgroundRepository rbRepo;

    @Autowired
    private RoomStyleRepository rsRepo;

    @Autowired
    private TagRepository  tRepo;

    @Autowired
    private DynamicReportRepository drRepo;

    @Autowired
    private DynamicRepository dRepo;

    @Autowired
    private AccountServiceImplementation  asImpl;

    @Autowired
    private RelationshipRepository relationshipRepo;

    @Autowired
    private TagRepository tagRepo;

    @Override
    public Object findMembersPage(Pageable pageable){
        return mRepo.findAll(pageable);
    }

    @Override
    public Object searchMembers(String keyword){
        return mRepo.findByCharacterIdLikeOrNicknameLike(keyword, keyword);
    }

    @Override
    public void activateOrDeMember(Long memberId, Member activated){
        Member m = mRepo.findOne(memberId);
        m.setActive(activated.getActive());
        mRepo.saveAndFlush(m);
    }

    @Override
    public Object findRoomsPage(Pageable pageable){
        Page<Room> rp = rRepo.findAll(pageable);
        List<Room> rs = rp.getContent();
        for(Room r : rs) {
            r.setMember(mRepo.findOne(r.getOwnerId()));
            r.setStyle(rsRepo.findOne(r.getStyleId()).getName());
        }
        return rs;
    }

    @Override
    public Object searchRooms(String keyword){
        List<Room> rs = rRepo.findByNameLike(keyword);
        for(Room r : rs) r.setMember(mRepo.findOne(r.getOwnerId()));
        return rs;
    }

    @Override
    public void updateARoom(Long roomId, Room updated){
        Room room = rRepo.findOne(roomId);
        if(updated.getActive() != null) room.setActive(updated.getActive());
        if(updated.getHotweight() != null) room.setHotweight(updated.getHotweight());
        rRepo.saveAndFlush(room);
    }

    @Override
    public Object findFeedbacks(Pageable pageable){
        Page<Feedback> fbp = fbRepo.findByRepliedFalse(pageable);
        List<Feedback> fbs = fbp.getContent();
        for(Feedback f : fbs) f.setMember(mRepo.findOne(f.getMemberId()));
        return fbs;
    }

    @Override
    public Object findMaterials(){
        return amRepo.findAll();
    }

    @Override
    public void deleteMaterial(Long materialId){
        amRepo.delete(materialId);
    }

    @Override
    public void addMaterial(AppMaterial material){
        material.setCreatedAt(new Date());
        amRepo.saveAndFlush(material);
    }

    @Override
    public Object findReports(Pageable pageable){
        Page<Report> rp = reRepo.findAll(pageable);
        List<Report> rl = rp.getContent();
        for(Report r : rl){
            r.setReporter(mRepo.findOne(r.getReporterId()));
            r.setReportMember(mRepo.findOne(r.getReportMemberId()));
        }
        return rl;
    }

    @Override
    public void replyFeedback(Long fid, String reply) throws Exception{
        Feedback f = fbRepo.findOne(fid);
        Member m = mRepo.findOne(f.getMemberId());
        RongCloud rc = new RongCloud();
        if(rc.sendCustomerMessage(reply, "1", m.getCharacterId())){
            f.setReplied(true);
            fbRepo.saveAndFlush(f);
        }
    }

    @Override
    public Object findRecharges(Pageable pageable){
        Page<Recharge> rp = rgRepo.findAll(pageable);
        List<Recharge> rl = rp.getContent();
        for(Recharge r : rl) r.setMember(mRepo.findOne(r.getMemberId()));
        return rl;
    }

    @Override
    public void manualRecharge(Member m) throws ObjectNotFoundException{
        Member e = mRepo.findByCharacterId(m.getCharacterId());
        if(e == null) throw new ObjectNotFoundException(ErrorMsg.NO_MEMBER);
        e.setCoins(e.getCoins() + m.getCoins());
        mRepo.saveAndFlush(e);
        Recharge rg = new Recharge();
        rg.setMemberId(e.getId());
        rg.setState(Recharge.OState.success);
        rg.setTransactionId("AI"+new Timestamp(System.currentTimeMillis()).getTime());
        rg.setOutTransactionCode("AO"+new Timestamp(System.currentTimeMillis()).getTime());
        rg.setAmount(m.getCoins().doubleValue());
        rg.setThrough(Recharge.PayThrough.admin);
        rg.setCreatedAt(new Date());
        rg.setUpdatedAt(new Date());
        rgRepo.saveAndFlush(rg);
        ctr.recordRecharge(e.getId(),m.getCoins(),0L,true);
    }


    @Override
    public WebAdmin loginWA(String name, String password){
        return waRepo.findByNameAndPassword(name, Encryption.MD5ed(password));
    }

    @Override
    public WebAdmin validateToken(String token){
        Long id = Long.parseLong(Jwts.parser().setSigningKey(Constant.getPrivateKey()).parseClaimsJws(token).getBody().getSubject());
        return waRepo.findOne(id);
    }

    @Override
    public Object findAnalytics(){
        Map<String, Object> m = new HashMap<String, Object>();
        int i = 0;
        while(i < 5){
            Calendar calendar = General.midnight();
            calendar.add(Calendar.HOUR, -24 * i);
            Date start = calendar.getTime();
            Calendar calendar2 = General.midnight();
            calendar2.add(Calendar.HOUR, 24 * (1-i));
            Date end = calendar2.getTime();
            m.put("day"+i+"member", mRepo.countByCreatedAtAfterAndCreatedAtBefore(start, end));
            m.put("day"+i+"room", rRepo.countByCreatedAtAfterAndCreatedAtBefore(start, end));
            i += 1;
        }
        m.put("totalmember", (int)mRepo.count());
        m.put("totalroom", (int)rRepo.count());
        m.put("totalrecharge", rgRepo.sumRecharge());
        return m;
    }


    @Override
    public Object findApprovalsPageByState(Integer state, String type,Pageable pageable) {

        if (type.equals("member")){
            List<Approval> memberApprovals = aRepo.findAllByMemberAndState(state, pageable);
            for (Approval approval: memberApprovals) approval.setMember(mRepo.findOne(approval.getMemberId()));
            return memberApprovals;
        }else{
          List<Approval> roomApprovals=aRepo.findAllByRoomAndState(state, pageable);
            for (Approval approval :roomApprovals) approval.setRoom(rRepo.findOne(approval.getRoomId()));
            return roomApprovals;

        }
    }

    @Override
    public void updateApprovalRejection(Long id, String description, String type) {
        if (type.equals("member")){
            aRepo.updateMemberApprovalRejection(id, description);
        }else{
            aRepo.updateRoomApprovalRejection(id, description);
        }
    }

    @Override
    public void updateApprovalPass(Long id, String type) {
        if (type.equals("member")){
            aRepo.updateMemberApprovalPass(id);
        }else{
            aRepo.updateRoomApprovalPass(id);
        }
    }

    @Override
    public void addRoomBackgroundList(String type, String[] url) {
        int t=1;
        if (type.equals("logo")) t=1;

        for (int i=0;i<url.length-1;i++) {
            rbRepo.addRoomBackground(t, url[i]);
        }


    }

    @Override
    public Object findRoomBackgrounds(Pageable pageable) {
        return rbRepo.findAll(pageable);
    }

    @Override
    public Object findRoomStylePage(Pageable pageable) {
        Page<RoomStyle> stylePage = rsRepo.findAll(pageable);
        for (RoomStyle r:stylePage ){
            if (r.getBgId().equals("")) continue;
            String[] split = r.getBgId().split(",");
            for (String s:split){
                r.getRbSet().add(rbRepo.findOne(Long.parseLong(s)));
            }
        }
        return  stylePage;
    }

    @Override
    public void deleteBackgrounds(List<Long> select) {
        select.forEach(x-> rbRepo.delete(x));
    }

    @Override
    public void addRoomStyle(RoomStyle roomStyle) {
        rsRepo.saveAndFlush(roomStyle);
    }

    @Override
    public void updateRoomStyle(RoomStyle roomStyle) {
        rsRepo.saveAndFlush(roomStyle);
    }

    @Override
    public void deleteRoomStyle(RoomStyle roomStyle) {
        rsRepo.delete(roomStyle.getId());
    }


    @Override
    public Object findTagsPage(Pageable pageable) {
        return  tRepo.findAll(pageable);
    }

    @Override
    public void updateTag(Tag tag) {
        tRepo.saveAndFlush(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tRepo.delete(id);
    }

    @Override
    public void addTag(Tag tag) {
        tRepo.saveAndFlush(tag);
    }


    @Override
    public Object findDynamicReports(Pageable pageable) {

        return drRepo.findAllByHandler(pageable.getPageSize()*pageable.getOffset(),pageable.getPageSize());
    }

    @Override
    public void cancelDynamicReport(Long dynamicReportId) {
        DynamicReport one = drRepo.findOne(dynamicReportId);
        if (one!=null){
            one.setHandler(1);//取消
            one.setUpdatedAt(new Date());
            drRepo.saveAndFlush(one);
        }
    }

    @Transactional
    @Override
    public void affirmDynamicReport(Long dynamicId, Long dynamicReportId) {
        Dynamic dynamic = dRepo.findOne(dynamicId);
        DynamicReport dynamicReport = drRepo.findOne(dynamicReportId);
        if (dynamic!=null){
            drRepo.updateISHide(dynamic.getId());
            dynamicReport.setUpdatedAt(new Date());
            dynamicReport.setHandler(2);//处理
            drRepo.saveAndFlush(dynamicReport);
        }
    }

    @Override
    public Object fackMember(Member member) {
        asImpl.generalSignUp(member);
        member.setAffectiveState(Member.AffectiveState.Single);
        member.setApToken("123");
        List<Long> tagIds = tagRepo.findIds();
        int total=(int)(Math.random()*(tagIds.size()/10)+1);
        StringBuilder sb=new StringBuilder("");
        for (int i=0;i<=total;i++){
            int index = (int)(Math.random()*tagIds.size()-1);
            sb.append(tagIds.get(index).toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        member.setTagId(sb.toString());
        Member m = mRepo.saveAndFlush(member);
        Relationship r = new Relationship();
        r.setFollowerId(member.getId());
        r.setFollowingId((long) 1);
        r.setCreatedAt(new Date());
        r.setUpdatedAt(new Date());
        relationshipRepo.saveAndFlush(r);
        return Http.standardResponse(m);
    }

    @Override
    public Object fackDynamic(Dynamic dynamic){
        List<Long> ids=mRepo.findFackMemberIds();
        int index =(int) Math.random()*ids.size();
        long memberId = ids.get(index).longValue();
        dynamic.setMemberId(memberId);
        long time = new Date().getTime();
        time-=(int)Math.random()*1000000000;
        Date date =new Date(time);
        dynamic.setTime(date);
        dynamic.setReadNumber(0);
        dynamic.setCommentNumber(0);
        dynamic.setStar(0);
        dynamic.setType(Dynamic.Type.open);
        Dynamic rs = dRepo.saveAndFlush(dynamic);
        return Http.standardResponse(rs);
    }
}


