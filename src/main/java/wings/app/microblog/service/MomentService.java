package wings.app.microblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wings.app.microblog.Exception.InexistenceException;
import wings.app.microblog.entity.*;
import wings.app.microblog.repository.*;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.util.RelationType;
import wings.app.microblog.ws.WebSocketServer;

import java.util.Date;
import java.util.List;

@Service
public class MomentService {

    @Autowired
    private MomentRepository momentRepo;

    @Autowired
    private StarRepository starRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private FavoriteRepository favoriteRepo;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private RelationShipService relationShipService;

    public Moment createMoment(Long id, Moment moment) {
        moment.setMemberId(id);
        moment.setTime(new Date());
        moment.setCommentNum(0);
        moment.setStarNum(0);
        return momentRepo.saveAndFlush(moment);
    }

    public Integer priseMoment(Long memberId, Long momentId) throws InexistenceException {
        Moment moment = momentRepo.findById(momentId).orElse(null);
        if (moment != null&&(moment.getVisibility()!= Moment.Visibility.none.getId())){
            Star star = starRepo.findByPriseMemberIdAndMomentId(memberId, moment.getId());
            if (star==null) {
                Star s = new Star();
                s.setFromId(memberId);
                s.setMomentId(momentId);
                s.setToId(moment.getId());
                s.setTime(new Date());
                s.setIsDelete(0);
                starRepo.saveAndFlush(s);
                moment.setStarNum(moment.getStarNum()+1);
                momentRepo.saveAndFlush(moment);
                return 1;
            }
            star.setIsDelete(star.getIsDelete()==0?1:0);
            starRepo.saveAndFlush(star);
            moment.setStarNum(star.getIsDelete()==1?moment.getStarNum()-1:moment.getStarNum()+1);
            return star.getIsDelete()==0?1:0;
        } else{
            throw new InexistenceException(ErrorCode.NO_MOMENT.msg);
        }

    }

    public void addComment(Member member, Comment comment) throws InexistenceException {
        Long momentId = comment.getMomentId();
        Moment moment= momentRepo.findById(momentId).orElse(null);
        if (moment==null|| (moment.getVisibility()== Moment.Visibility.none.getId()))
            throw new  InexistenceException(ErrorCode.NO_MOMENT.msg);
        else
            comment.setFromId(member.getId());
            comment.setTime(new Date());
            commentRepo.saveAndFlush(comment);
            moment.setCommentNum(moment.getCommentNum()+1);
            momentRepo.saveAndFlush(moment);
    }

    public List<Comment> getCommentList(Long momentId, Pageable pageable) throws InexistenceException {
        Moment moment= momentRepo.findById(momentId).orElse(null);
        if (moment==null|| (moment.getVisibility()== Moment.Visibility.none.getId()))
            throw new  InexistenceException(ErrorCode.NO_MOMENT.msg);
        List<Comment> commentList= commentRepo.findTopCommentList(momentId, pageable);
        commentList.forEach(c->{
            Member member = memberRepo.findById(c.getFromId()).orElse(null);
            c.setFromName(member.getNickname());
            c.setAvatarUrl(member.getAvatarUrl());

            List<Comment> parentList = commentRepo.findByParentIdList(c.getId(), momentId);
            parentList.forEach(x->{
                x.setFromName(memberRepo.findById(x.getFromId()).orElse(null).getNickname());
                x.setToName(memberRepo.findById(x.getToId()).orElse(null).getNickname());
            });
            c.setReplyList(parentList);
        });
        return commentList;
    }

    public void updateMomentPrivacy(Long momentId, Integer visibility) throws InexistenceException {
        Moment moment= momentRepo.findById(momentId).orElse(null);
        if (moment==null|| (moment.getVisibility()== Moment.Visibility.none.getId()))
            throw new  InexistenceException(ErrorCode.NO_MOMENT.msg);
        else
            moment.setVisibility(visibility);
            momentRepo.saveAndFlush(moment);
    }

    public List<Moment> getAllVisibleMoments(Long id, Member m, Pageable pageable) {
        List<Moment> moments = momentRepo.getAllVisibleMoments(id, pageable.getOffset(), pageable.getPageSize());
        if (moments!=null) {
            moments.forEach(x -> {
                Member member = memberRepo.findById(x.getMemberId()).orElse(null);
                x.setAvatarUrl(member.getAvatarUrl());
                x.setNickname(member.getNickname());
                Integer isPrise = starRepo.findIsPrise(id, x.getId());
                x.setIsPraise(isPrise);
                x.setIsMark(favoriteRepo.findByCollectorId(id, x.getId()) == null ? 0 : 1);
                Integer relation = relationShipService.getRelation(m, x.getMemberId());
                x.setRelation(relation);
            });
        }
        return moments;
    }

    public Integer collectMoment(Long memberId, Long momentId) {
        Favorite isExist = favoriteRepo.findByCollectorId(memberId, momentId);
        if(isExist==null) {
            Favorite favorite=new Favorite();
            favorite.setCollectorId(memberId);
            favorite.setMomentId(momentId);
            favoriteRepo.saveAndFlush(favorite);
            Moment m=momentRepo.findById(momentId).orElse(null);
            Msg msg=new Msg(); msg.setFromId(memberId);
            msg.setToId(m.getMemberId());
            msg.setMsg("你的帖子被收藏");
            msg.setType(0);
            msg.setTime(new Date());
            webSocketServer.sendMsg(msg);
            return 1;
        } else {
            favoriteRepo.delete(isExist);
            return 0;
        }

    }

    public List<Moment> findRelationMoments(Member member, Integer relation) {
        Integer visibility=3;
        if (relation== RelationType.following.getId()){
            visibility=2;
        }
        List<Moment> moments = momentRepo.findByRelation(member.getId(), visibility);

        moments.forEach(x->{
            x.setAvatarUrl(member.getAvatarUrl());
            x.setNickname(member.getNickname());
            Integer isPrise = starRepo.findIsPrise(member.getId(), x.getId());
            x.setIsPraise(isPrise);
            x.setIsMark(favoriteRepo.findByCollectorId(member.getId(), x.getId()) == null ? 0 : 1);
        });
        return moments;
    }


    public void createReport(Long id, Report report) {
        report.setReporterId(id);
        report.setTime(new Date());
        reportRepo.saveAndFlush(report);
    }
}


