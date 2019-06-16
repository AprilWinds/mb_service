package chat.hala.app.restful;


import chat.hala.app.entity.Member;
import chat.hala.app.library.util.ErrorCode;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.Http;
import chat.hala.app.restful.exception.ConflictException;
import chat.hala.app.restful.exception.NotAcceptableException;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.restful.exception.OutboundException;
import chat.hala.app.restful.wrapper.AccountWithToken;
import chat.hala.app.service.AccountService;
import chat.hala.app.service.ApprovalService;
import chat.hala.app.service.MemberService;
import chat.hala.app.service.RelationshipService;
import chat.hala.app.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by astropete on 2017/12/25.
 */


@RestController
@RequestMapping("/account")
public class AccountResource {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private RelationshipService relationshipService;

    @RequestMapping(value = "/sms", method = RequestMethod.POST, produces = "application/json")
    public Object sendSMS(@RequestParam("mobile") String mobile) throws OutboundException, NotAcceptableException {

        try {
            accountService.sendSmsCode(mobile);
            return Http.generalResponse();
        } catch (PreconditionNotQualifiedException e) {
            throw new NotAcceptableException(e.getMessage());
        } catch (ThirdPartyServiceException e) {
            throw new OutboundException(e.getMessage());
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public Object register(@RequestParam("code") String code,
                           @RequestBody Member member,
                           @RequestHeader("Accept-Language")String language) throws OutboundException, NotAcceptableException, ConflictException {
        try {
            Member m = accountService.signup(member, code);
            String at = accountService.issueToken(m, language);
            approvalService.insertApproval("member", m.getId());
            return new AccountWithToken(m, at, "register");
        } catch (PreconditionNotQualifiedException e) {
            throw new NotAcceptableException(e.getMessage());
        } catch (ObjectAlreadyExistedException e) {
            throw new ConflictException(e.getMessage());
        } catch (ObjectIllegalException e) {
            throw new NotAcceptableException(e.getMessage());
        } catch (RongCloudException e) {
            throw new OutboundException(ErrorMsg.RONG_CLOUD_ERR);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public Object login(@RequestParam("reference") String reference,
                        @RequestParam("password") String password,
                        @RequestHeader("Accept-Language")String language) throws NotFoundException {
        try {

            Member m = accountService.signin(reference, password);
            String at = accountService.issueToken(m, language);
            String behavior = approvalService.checkApprovalState(m.getId());

            return new AccountWithToken(m, at, behavior);
        } catch (ObjectNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

  /*  @RequestMapping(value = "/3rdparty", method = RequestMethod.POST, produces = "application/json")
    public Object thirdPartySignInOrSignUp(@RequestParam("from") String from,
                                           @RequestParam("outId") String outId) throws Exception{
        Member m = accountService.thirdPartySignIn(from, outId);
        String s = "login";
        if(m.getId() == null){
            m = accountService.thirdPartySignUp(from, m);
            s = "register";
        }else{
            s=approvalService.checkApprovalState(m.getId());
        }
        return new AccountWithToken(m, accountService.issueToken(m), s);
    }
*/

    @RequestMapping(value = "/appearance", method = RequestMethod.GET, produces = "application/json")
    public Object getAvatar(@RequestParam("reference") String reference) {
        Member m = accountService.getMemberByReference(reference);
        Map<String, String> re = new HashMap<>();
        re.put("avatarUrl", m == null ? "" : m.getAvatarUrl());
        return re;
    }


    @RequestMapping(value = "/thirdParty", method = RequestMethod.POST, produces = "application/json")
    public Object thirdParty(@RequestParam("from") String from, @RequestParam("thirdId") String thirdId,
                             @RequestParam("lng") float lng, @RequestParam("lat") float lat,
                             @RequestParam("thirdToken")String thirdToken,
                             @RequestHeader("Accept-Language")String language)  {

        Member m=accountService.thirdSigIn(from,thirdId);
        String s = "login";
        if(m == null){

            try {
                m = accountService.thirdSignUp(from,thirdId,thirdToken);
            } catch (ThirdPartyServiceException e) {
                return  Http.standardResponse(ErrorCode.THIRD_PARTY_ERROR.getStatus(),ErrorCode.THIRD_PARTY_ERROR.getMsg());
            } catch (RongCloudException e) {
                return  Http.standardResponse(ErrorCode.RONGCLOUD_ERROR.getStatus(),ErrorCode.RONGCLOUD_ERROR.getMsg());
            }
            s = "register";
        }
        memberService.updateMemberLocate(m,lng,lat);
        return Http.standardResponse(new AccountWithToken(m, accountService.issueToken(m,language), s),200,"success");
   }


}