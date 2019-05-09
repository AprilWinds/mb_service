package wings.app.microblog.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wings.app.microblog.entity.*;
import wings.app.microblog.repository.*;
import wings.app.microblog.util.General;
import wings.app.microblog.ws.WebSocketServer;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WAService {

    @Autowired
    private WebAdminRepository waRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private MomentRepository momentRepo;

    @Autowired
    private FeedBackRepository feedBackRepo;

    @Autowired
    private MsgRepository msgRepo;

    @Autowired
    private  WebSocketServer ws;

    public WebAdmin validateToken(String token) {
        Long id=null;
        try {
            id = Long.parseLong(Jwts.parser().setSigningKey(General.privateKey).parseClaimsJws(token).getBody().getSubject());
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return waRepo.findById(id).orElse(null);
    }

  public void  createToken(WebAdmin wa) {
        assert wa != null;
    String compact = Jwts.builder().setSubject(String.valueOf(wa.getId())).setId(UUID.randomUUID().toString())
            .signWith(SignatureAlgorithm.HS256, General.privateKey).compact();
        wa.setAppToken(compact);
    }

    public WebAdmin checkWA(WebAdmin wa) {
        WebAdmin w = waRepo.findByContent(wa.getUsername(), wa.getPassword());
        return w;
    }

    public List<Member> getMemberList(Pageable pageable) {

        return memberRepo.findMemberByPage(pageable);
    }

    public void activateMember(Long memberId, Integer active) {
        Member member = memberRepo.findById(memberId).orElse(null);
        if (member!=null){
            member.setIsActive(active);
            memberRepo.saveAndFlush(member);
        }
    }

    public List<Member> searchMember(String key) {

        return  memberRepo.findByKey(key);
    }

    public List<Report> getReportList(Pageable pageable) {
        List<Report> ls = reportRepo.findByPage(pageable);
        ls.forEach(x->{
            Member reporter = memberRepo.findById(x.getReporterId()).orElse(null);
            x.setReporterName(reporter.getNickname());
            Member reportMember=memberRepo.findById(x.getReportMemberId()).orElse(null);
            x.setReportMemberName(reportMember.getNickname());
            Moment moment = momentRepo.findById(x.getMomentId()).orElse(null);
            x.setImg(moment.getImgUrl());
            x.setContent(moment.getContent());
        });
        return  ls;
    }

    public void opReport(Long reportId, String op) {
        Report report = reportRepo.findById(reportId).orElse(null);
        if (op.equals("删除")){
            Moment moment = momentRepo.findById(report.getMomentId()).orElse(null);
            moment.setVisibility(0);
            momentRepo.saveAndFlush(moment);
        }
        reportRepo.deleteById(report.getId());
    }

    public List<Feedback> getFeedbackList(Pageable pageable) {
        List<Feedback> ls = feedBackRepo.findByPage(pageable);
        ls.forEach(x->{
            Member m = memberRepo.findById(x.getMemberId()).orElse(null);
            x.setNickname(m.getNickname());
        });
        return ls;
    }

    public void delFeedback(Long fid) {
        feedBackRepo.deleteById(fid);
    }

    public void publish(Msg msg) {
        msg.setTime(new Date());
        msg.setFromNickname("wings");
        msg.setFromAvatar("");
        msg.setToId((long) 0);
        msg.setFromId((long) 0);
        ws.sendAll(msg);
    }
}
