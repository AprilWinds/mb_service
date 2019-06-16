package wings.app.microblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wings.app.microblog.entity.Favorite;
import wings.app.microblog.entity.Feedback;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Moment;
import wings.app.microblog.repository.*;
import wings.app.microblog.util.General;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private RelationShipRepository relationShipRepo;

    @Autowired
    private MomentRepository momentRepo;

    @Autowired
    private FavoriteRepository favoriteRepo;

    @Autowired
    private StarRepository starRepo;

    @Autowired
    private RelationShipService relationShipService;

    @Autowired
    private MomentService momentService;

    @Autowired
    private FeedBackRepository feedBackRepo;

    public Member updateProfile(Member member, Member updated){
        member.setNickname(updated.getNickname() != null ? updated.getNickname() : member.getNickname());
        member.setAvatarUrl(updated.getAvatarUrl() != null ? updated.getAvatarUrl() : member.getAvatarUrl());
        member.setDob(updated.getDob() != null ? updated.getDob() : member.getDob());
        member.setGender(updated.getGender() != null ? updated.getGender() : member.getGender());
        member.setIntroduction(updated.getIntroduction() != null ? updated.getIntroduction() : member.getIntroduction());
        member.setAddress(updated.getAddress() != null ? updated.getAddress() : member.getAddress());
        member.setCompany(updated.getCompany()!=null ?updated.getCompany():member.getCompany());
        member.setUpdatedAt(new Date());

        Member m = memberRepo.saveAndFlush(member);
        m.setFansCount(relationShipRepo.findAllFans(m.getId()));
        m.setFollowingCount(relationShipRepo.findAllFollow(m.getId()));
        m.setMomentsCount(momentRepo.getMomentsNum(m.getId()));

        return m;
    }

    public Member getMemberInfo(Member member, Long memberId) throws ParseException {
        if (member.getId().longValue()==memberId){
            member.setAge(General.getAgeByBirth(member.getDob()));
            member.setFansCount(relationShipRepo.findAllFans(member.getId()));
            member.setFollowingCount(relationShipRepo.findAllFollow(member.getId()));
            member.setMomentsCount(momentRepo.getMomentsNum(member.getId()));
            return member;
        }else{
           Member m= memberRepo.findById(memberId).orElse(null);
           m.setAge(General.getAgeByBirth(m.getDob()));
           m.setFansCount(relationShipRepo.findAllFans(memberId));
           m.setFollowingCount(relationShipRepo.findAllFollow(memberId));
           m.setMomentsCount(momentRepo.getMomentsNum(m.getId()));
           Integer relation = relationShipService.getRelation(member, memberId);
           m.setRelation(relation);
           List<Moment> relationMoments = momentService.findRelationMoments(m, relation);
           m.setMoments(relationMoments);

           return m;
        }
    }

    public List<Moment> getMyMoments(Member member) {
        List<Moment> ls = momentRepo.findByMemberIdAndVisibility(member.getId());
        ls.forEach(x->{
            x.setAvatarUrl(member.getAvatarUrl());
            x.setNickname(member.getNickname());
            x.setIsPraise(0);
            x.setIsMark(0);
        });
        return ls;
    }

    public List<Moment> getMyFavorite(Long id) {
        List<Favorite> myFavorite = favoriteRepo.findMyFavorite(id);
        List<Moment> ls=new ArrayList<>();
        myFavorite.forEach(x->{
            Long momentId = x.getMomentId();
            Moment moment = momentRepo.findById(momentId).orElse(null);
            Member member = memberRepo.findById(moment.getMemberId()).orElse(null);
            moment.setNickname(member.getNickname());
            moment.setIsMark(1);
            moment.setAvatarUrl(member.getAvatarUrl());
            Integer isPrise = starRepo.findIsPrise(id, x.getId());
            moment.setIsPraise(isPrise);
            ls.add(moment);
        });

        return ls;
    }

    public void feedback(Feedback feedBack) {
        feedBack.setTime(new Date());
        feedBackRepo.saveAndFlush(feedBack);
    }

    public List<Member> search(String ky, Member member) {
        List<Member> ls = memberRepo.findByKey(ky);
        ls.forEach(x->{
            Integer relation = relationShipService.getRelation(member, x.getId());
            x.setRelation(relation);
        });
        return ls;
    }
}
