package chat.hala.app.service.implementation;

import chat.hala.app.entity.Blocking;
import chat.hala.app.entity.Member;
import chat.hala.app.entity.Relationship;
import chat.hala.app.entity.SystemNotice;
import chat.hala.app.library.util.*;
import chat.hala.app.repository.BlockingRepository;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.RelationshipRepository;
import chat.hala.app.repository.SystemNoticeRepository;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.service.RelationshipService;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Created by astropete on 2018/1/25.
 */

@Service
public class RelationshipServiceImplementation implements RelationshipService{

    @Autowired
    private RelationshipRepository relationshipRepo;

    @Autowired
    private BlockingRepository blockingRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private CloudUtils cu;

    @Autowired
    private SystemNoticeRepository snRepo;

    @Override
    public Object getFollowingByMember(Long memberId, Pageable pageable){
        List<Long> followingIds = new ArrayList<>();
        Page<Relationship> relations = relationshipRepo.findByFollowerId(memberId, pageable);
        if(!relations.hasContent()) return Http.standardResponse(followingIds);//General.pageImitation(relations, new ArrayList<>());
        for(Relationship r : relations.getContent()) followingIds.add(r.getFollowingId());
        return  Http.standardResponse(this.getMemberByIds(followingIds));
        //General.pageImitation(relations, this.getMemberByIds(followingIds));
    }

    @Override
    public Object getFanByMember(Long memberId, Pageable pageable){
        List<Long> fanIds = new ArrayList<>();
        Page<Relationship> relations = relationshipRepo.findByFollowingId(memberId, pageable);
        if(!relations.hasContent()) return  Http.standardResponse(fanIds);//General.pageImitation(relations, new ArrayList<>());
        for(Relationship r : relations.getContent()) fanIds.add(r.getFollowerId());
        return Http.standardResponse(this.getMemberByIds(fanIds));
        // return General.pageImitation(relations, this.getMemberByIds(fanIds));
    }

    @Override
    public Object getFriendsByMember(Long memberId, Pageable pageable) {
        List<Long> ls= relationshipRepo.findFriendId(memberId,pageable.getOffset(),pageable.getPageSize());
        if (ls.size()==0||ls==null){return Http.standardResponse(new ArrayList<Long>());}
        return Http.standardResponse(this.getMemberByIds(ls));
    }

    @Override
    public Object getBlockingByMember(Member member){
        List<Long> blockingMemberIds = new ArrayList<>();
        List<Blocking> blockings = blockingRepo.findByBlockerIdOrderByCreatedAtDesc(member.getId());
        if(blockings.size() == 0) return Http.standardResponse(blockingMemberIds);//new ArrayList<>();
        for(Blocking b : blockings) blockingMemberIds.add(b.getBlockingMemberId());
        return Http.standardResponse(this.getMemberByBlocking(blockings,blockingMemberIds));
        //return this.getMemberByIds(blockingMemberIds);
    }

    @Override
    public void followMember(Member member, Long relatedId, String language) throws PreconditionNotQualifiedException {
        Relationship er = relationshipRepo.findByFollowerIdAndFollowingId(member.getId(), relatedId);
        if (er != null) return;
        Blocking b1 = blockingRepo.findByBlockerIdAndBlockingMemberId(member.getId(), relatedId);
        Blocking b2 = blockingRepo.findByBlockerIdAndBlockingMemberId(relatedId, member.getId());
        if (b1 != null || b2 != null) {
            throw new PreconditionNotQualifiedException(ErrorMsg.BLOCKING_EXISTED);
        }
        Relationship r = new Relationship();
        r.setFollowerId(member.getId());
        r.setFollowingId(relatedId);
        r.setCreatedAt(new Date());
        r.setUpdatedAt(new Date());
        relationshipRepo.saveAndFlush(r);

        StringBuilder sb=new StringBuilder("");
        if (language.equals(Constant.LAN_ARAB)){
            sb.append("يتابعك الآن فقط");
        }else {
            sb.append(" followed you just now");
        }
        SystemNotice sn=new SystemNotice();
        sn.setIsRead(0);
        sn.setTime(new Date());
        sn.setMsg(sb.toString());
        sn.setDynamicId((long) 0);
        sn.setMemberId(member.getId());
        sn.setReceiveId(relatedId);
        snRepo.saveAndFlush(sn);
        Member toMember = memberRepo.findOne(relatedId);
        String[] target = {String.valueOf(toMember.getCharacterId())};
        try {
            StringBuilder s1=new StringBuilder(member.getNickname());
            s1.append(sb.toString());
            cu.systemNotice(s1.toString(),target,"{\"type\":0}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unfollowMember(Member member, Long relatedId){
        Relationship r = relationshipRepo.findByFollowerIdAndFollowingId(member.getId(), relatedId);
        if(r != null) relationshipRepo.delete(r);
    }

    @Override
    public void blockMember(Member member, Long relatedId) throws NotFoundException {
        Member one = memberRepo.findOne(relatedId);
        if (one==null){throw new NotFoundException(ErrorMsg.NO_MEMBER);}
        Blocking eb = blockingRepo.findByBlockerIdAndBlockingMemberId(member.getId(), relatedId);
        if(eb != null) return ;
        Blocking b = new Blocking();
        b.setBlockerId(member.getId());
        b.setBlockingMemberId(relatedId);
        b.setCreatedAt(new Date());
        b.setUpdatedAt(new Date());
        blockingRepo.saveAndFlush(b);
        Relationship r = relationshipRepo.findByFollowerIdAndFollowingId(member.getId(), relatedId);
        if(r != null) relationshipRepo.delete(r);
    }

    @Override
    public void unblockMember(Member member, Long relatedId){
        Blocking b = blockingRepo.findByBlockerIdAndBlockingMemberId(member.getId(), relatedId);
        if(b != null) blockingRepo.delete(b);
    }

    private List<Member> getMemberByIds(List<Long> ids){
        List<Member> members = memberRepo.findAllByIdListOrderByIdList(ids);
        for(Member m : members) {
            m.setFollowingCount(relationshipRepo.findCountOfFollowing(m.getId()));
            m.setFanCount(relationshipRepo.findCountOfFan(m.getId()));
            m.discardSensitive();
            String age=null;
            try {
               age =General.getAgeByBirth(m.getDob());
            } catch (ParseException e) {
                age="";
            }
            m.setAge(age);
        }
        return members;
    }


    private List<Member> getMemberByBlocking(List<Blocking> blockings, List<Long> ids){


        List<Member> members = memberRepo.findAllByIdListOrderByIdList(ids);
        int i=0;
        for(Member m : members){
            m.setFollowingCount(relationshipRepo.findCountOfFollowing(m.getId()));
            m.setFanCount(relationshipRepo.findCountOfFan(m.getId()));
            m.discardSensitive();
            m.setBlockingTime(blockings.get(i).getCreatedAt());
            i++;
        }
        return members;
    }

}
