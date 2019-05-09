package wings.app.microblog.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wings.app.microblog.Exception.BlockedException;
import wings.app.microblog.Exception.RepetitionException;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.entity.Member;
import wings.app.microblog.service.AccountService;
import wings.app.microblog.util.Http;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/account")
public class AccountResource {

    @Autowired
    public AccountService accountService;


    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request) {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public Object register(@RequestBody Member member) {

        try {
            accountService.createMember(member);
        } catch (RepetitionException e) {
            return Http.standardResponse(ErrorCode.USERNAME_REPETITION.status,ErrorCode.USERNAME_REPETITION.msg);
        }
        accountService.createToken(member);
        return Http.standardResponse(member) ;
    }

    @RequestMapping(value = "/enter", method = RequestMethod.POST, produces = "application/json")
    public Object enter(@RequestBody Member member) {
        Member m = accountService.checkMember(member);
        if (m != null) {
            if (m.getIsActive() == 0) {
                return Http.standardResponse(ErrorCode.BANNED.status, ErrorCode.BANNED.msg);
            }
            accountService.createToken(m);
            return Http.standardResponse(m);
        }
        return Http.standardResponse(ErrorCode.LOGIN_ERROR.status, ErrorCode.LOGIN_ERROR.msg);
    }


}
