package wings.app.microblog.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wings.app.microblog.entity.Feedback;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.Moment;
import wings.app.microblog.service.MemberService;
import wings.app.microblog.util.Http;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberResource {

    @Autowired
    private MemberService memberService;



    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request) {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = "application/json")
    public Object update(@ModelAttribute("member") Member member,
                         @RequestBody Member updated){

        Member m = memberService.updateProfile(member, updated);
        return Http.standardResponse(m);
    }

    @RequestMapping(value = "/{memberId}", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberInfo(@ModelAttribute("member") Member member,
                                @PathVariable("memberId") Long memberId){
        Member m=null;
        try {
            m  = memberService.getMemberInfo(member, memberId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Http.standardResponse(m);
    }

    @RequestMapping(value="/myMoments",method =RequestMethod.GET,produces = "application/json")
    public  Object getMyMoments(@ModelAttribute("member") Member member){
        List<Moment> ls = memberService.getMyMoments(member);
        return Http.standardResponse(ls);
    }

    @RequestMapping(value = "/myFavorite",method = RequestMethod.GET,produces = "application/json")
    public  Object getMyFavorite(@ModelAttribute("member") Member member){
        List<Moment> ls = memberService.getMyFavorite(member.getId());
        return  Http.standardResponse(ls);
    }

    @RequestMapping(value = "/myFollow",method = RequestMethod.GET,produces = "application/json")
    public  Object getMyFollow(@ModelAttribute("member") Member member){
        List<Moment> ls = memberService.getMyFavorite(member.getId());
        return  Http.standardResponse(ls);
    }


    @RequestMapping(value = "/feedback",method = RequestMethod.POST,produces = "application/json")
    public  Object feedback(@RequestBody Feedback feedBack){
        memberService.feedback(feedBack);
        return  Http.standardResponse();
    }

}

