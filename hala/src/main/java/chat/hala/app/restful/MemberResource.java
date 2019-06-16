package chat.hala.app.restful;

import chat.hala.app.entity.*;
import chat.hala.app.library.util.ErrorCode;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.Http;
import chat.hala.app.restful.exception.ConflictException;
import chat.hala.app.restful.exception.NotAcceptableException;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.restful.exception.OutboundException;
import chat.hala.app.service.MemberService;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by astropete on 2017/12/26.
 */

@RestController
@RequestMapping("/member")
public class MemberResource {

    @Autowired
    private MemberService memberService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = "application/json")
    public Object update(@ModelAttribute("member") Member member,
                         @RequestBody Member updated,HttpServletRequest request){

        String language = request.getHeader("Accept-Language");
        return Http.standardResponse( memberService.updateAMember(member, updated,language));
    }

    @RequestMapping(value = "/token/ap", method = RequestMethod.POST, produces = "application/json")
    public Object setMemberPushToken(@ModelAttribute("member") Member member,
                                     @RequestBody Member tokenM){
        memberService.setMemberPushToken(member, tokenM);
        return Http.generalResponse();
    }

    @RequestMapping(value = "/token/rong", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberRongToken(@ModelAttribute("member") Member member) throws OutboundException{
        try{
            return memberService.getMemberRongToken(member);
        } catch (Exception e){
            throw new OutboundException(ErrorMsg.RONG_CLOUD_ERR);
        }
    }

    @RequestMapping(value = "/{memberId}", method = RequestMethod.GET, produces = "application/json")
    public Object getMember(@ModelAttribute("member") Member member,
                            @PathVariable("memberId") Long memberId,
                            HttpServletRequest request) throws OutboundException{

            String language = request.getHeader("Accept-Language");
        Member m = null;
        try {
            m = memberService.getMemberById(member, memberId, language);
            return Http.standardResponse(m,200,"success");
        } catch (NotFoundException e) {
            return Http.standardResponse(ErrorCode.NO_MEMBER.getStatus(),ErrorCode.NO_MEMBER.getMsg());
        }

    }

    @RequestMapping(value = "/character/{characterId}", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberByCharacterId(@ModelAttribute("member") Member member,
                                         @PathVariable("characterId") String characterId,
                                         HttpServletRequest request) throws OutboundException{
        try {
            String language = request.getHeader("Accept-Language");
            Member m = memberService.getMemberByCharacterId(member, characterId, language);
            if (m==null){return  Http.standardResponse(ErrorCode.NO_MEMBER.getStatus(),ErrorCode.NO_MEMBER.getMsg());}
            return Http.standardResponse(m);
        } catch (Exception e){
            throw new OutboundException(ErrorMsg.RONG_CLOUD_ERR);
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public Object searchMember(@ModelAttribute("member") Member member,
                               @RequestParam("keyword") String keyword,
                               @PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return memberService.searchMembersByKeyword(keyword, pageable);
    }

    @RequestMapping(value = "/fonding", method = RequestMethod.POST, produces = "application/json")
    public Object createMemberFonding(@ModelAttribute("member") Member member,
                                      @RequestParam("fondingIds") String fondingIds){
        return memberService.addMemberFondings(member, fondingIds);
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST, produces = "application/json")
    public Object bind(@ModelAttribute("member") Member member,
                       @RequestParam("code") String code,
                       @RequestBody Member bind) throws ConflictException, NotAcceptableException{
        try {
            return memberService.bindAMember(member, code, bind);
        } catch (PreconditionNotQualifiedException e){
            throw new NotAcceptableException(e.getMessage());
        } catch (ObjectAlreadyExistedException e){
            throw new ConflictException(e.getMessage());
        }
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST, produces = "application/json")
    public Object reportAMember(@ModelAttribute("member") Member member,
                                @RequestBody Report report){
        memberService.reportMember(member, report);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/locate", method = RequestMethod.PUT, produces = "application/json")
    public Object updateMemberLocate(@ModelAttribute("member") Member member,
                                     @RequestParam("lng") float longitude,
                                     @RequestParam("lat") float latitude){
        memberService.updateMemberLocate(member, longitude, latitude);
        return Http.generalResponse();
    }

    @RequestMapping(value = "/setting", method = RequestMethod.PUT, produces = "application/json")
    public Object updateMemberSetting(@ModelAttribute("member") Member member,
                                      @RequestBody MemberSetting setting){
        return memberService.updateMemberSetting(member, setting);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberSetting(@ModelAttribute("member") Member member){
        return memberService.getMemberSetting(member);
    }

    @RequestMapping(value = "/checkin", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberCheckin(@ModelAttribute("member") Member member){
        return memberService.getCheckinStatus(member);
    }

    @RequestMapping(value = "/checkin", method = RequestMethod.POST, produces = "application/json")
    public Object memberCheckin(@ModelAttribute("member") Member member) throws ConflictException{
        try{
            memberService.checkin(member);
            return Http.generalResponse();
        } catch (ObjectAlreadyExistedException e){
            throw new ConflictException(e.getMessage());
        }
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST, produces = "application/json")
    public Object proposeFeedback(@ModelAttribute("member") Member member,
                                  @RequestBody Feedback feedback){
        memberService.proposeFeedback(member, feedback);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    public Object logoutAMember(@ModelAttribute("member") Member member){
        memberService.logout(member);
        return  Http.standardResponse(null);
    }


    @RequestMapping (value = "/tags",method = RequestMethod.GET,produces = "application/json")
    public Object  getTags(HttpServletRequest request){
        String language = request.getHeader("Accept-Language");
        return  memberService.getTags(language);
    }

    @RequestMapping(value = "/addTag",method = RequestMethod.POST,produces = "application/json")
    public Object addTag(@ModelAttribute ("member")Member member,@RequestParam("arg")String arg){
        return  memberService.addTag(member,arg);
    }

    @RequestMapping(value = "/updateTag",method = RequestMethod.POST,produces = "application/json")
    public Object updateTag(@ModelAttribute("member")Member member,@RequestParam("arg")String arg){
        return memberService.updateTag(member,arg);
    }


    @RequestMapping(value = "/deleteTag",method = RequestMethod.POST, produces = "application/json")
    public  Object deleteTag(@ModelAttribute("member")Member member,@RequestParam("arg")String arg){
        return  memberService.deleteTag(member,arg);
    }

    @RequestMapping(value ="/{memberId}/dynamics",method = RequestMethod.GET,produces ="application/json")
    public Object getMyDynamics(@ModelAttribute("member")Member member,@PathVariable("memberId")Long memberId,@PageableDefault(size = 10)Pageable pageable) throws NotFoundException {
        try {
            return memberService.findDynamics(member,memberId,pageable);
        } catch (NotFoundException e) {
           return  Http.standardResponse(ErrorCode.NO_MEMBER.getStatus(),ErrorCode.NO_MEMBER.getMsg());
        }
    }

    @RequestMapping(value = "/notice",method = RequestMethod.GET,produces = "application/json")
    public  Object  getSystemNotice(@ModelAttribute("member")Member member,@PageableDefault(size = 10)Pageable pageable){
        return  memberService.getSystemNotice(member,pageable);
    }


    @RequestMapping(value ="/{noticeId}/read",method = RequestMethod.POST,produces = "application/json")
    public Object updateNotice(@PathVariable("noticeId")Long noticeId,@ModelAttribute("member")Member member){
        return memberService.readNoticeList(noticeId,member.getId());
    }


    @RequestMapping(value = "/throwBottle",method = RequestMethod.POST,produces = "application/json")
    public Object throwBottle(@ModelAttribute("member")Member member, @RequestParam("content")String content){
        if (member.getSendCount()==0){
            return Http.standardResponse(ErrorCode.NUMBER_LESS_THAN.getStatus(),ErrorCode.NUMBER_LESS_THAN.getMsg());
        }

        return memberService.createBottle(content,member);
    }

    @RequestMapping(value = "/salvageBottle",method = RequestMethod.GET,produces = "application/json")
    public Object salvageBottle(@ModelAttribute("member")Member member){
        return memberService.getBottle(member);
    }
}
