package wings.app.microblog.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import wings.app.microblog.entity.*;
import wings.app.microblog.service.WAService;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.util.Http;

import java.util.List;

@RestController
@RequestMapping("/wa")
public class WAResource {

    @Autowired
    private WAService waService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public Object enter(@RequestBody WebAdmin wa) {
        WebAdmin webAdmin = waService.checkWA(wa);
        if (webAdmin != null) {
            waService.createToken(wa);
            return Http.standardResponse(wa);
        }
        return Http.standardResponse(ErrorCode.LOGIN_ERROR.status, ErrorCode.LOGIN_ERROR.msg);
    }

    @RequestMapping(value = "/member", method = RequestMethod.GET,produces = "application/json")
    public Object getMember(@PageableDefault(value = 10)Pageable pageable) {
        List<Member> members = waService.getMemberList(pageable);
        return Http.standardResponse(members);
    }

    @RequestMapping(value = "/member/{memberId}/activate",method = RequestMethod.POST)
    public Object activateMember(@PathVariable("memberId")Long memberId,@RequestParam("active") Integer active) {
        waService.activateMember(memberId,active);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/member/search",method = RequestMethod.GET)
    public Object searchMember(@RequestParam("keyword") String key ) {
        List<Member> members = waService.searchMember(key);
        return Http.standardResponse(members);
    }

    @RequestMapping(value = "/report",method = RequestMethod.GET,produces = "application/json")
    public Object reportMoment(@PageableDefault(value = 10)Pageable pageable) {
        List<Report> ls = waService.getReportList(pageable);
        return Http.standardResponse(ls);
    }

    @RequestMapping(value = "/report/{reportId}/op",method = RequestMethod.POST)
    public Object  opReport (@PathVariable("reportId")Long reportId,@RequestParam("op")String op) {
        waService.opReport(reportId,op);
        return Http.standardResponse();
    }


    @RequestMapping(value = "/feedback",method = RequestMethod.GET,produces = "application/json")
    public Object  getFeedbacks(@PageableDefault(value = 10)Pageable pageable) {
        List<Feedback> ls = waService.getFeedbackList(pageable);
        return Http.standardResponse(ls);
    }

    @RequestMapping(value = "/feedback/{feedbackId}",method = RequestMethod.POST,produces = "application/json")
    public Object  delFeedback(@PathVariable("feedbackId")Long fid) {
        waService.delFeedback(fid);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/publish",method = RequestMethod.POST,produces = "application/json")
    public Object  publish(@RequestBody Notification msg) {
        waService.publish(msg);
        return Http.standardResponse();
    }
}