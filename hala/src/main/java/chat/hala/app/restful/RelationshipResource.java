package chat.hala.app.restful;

import chat.hala.app.entity.Member;
import chat.hala.app.library.util.ErrorCode;
import chat.hala.app.library.util.Http;
import chat.hala.app.restful.exception.NotAcceptableException;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.service.RelationshipService;
import chat.hala.app.service.exception.ObjectNotFoundException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by astropete on 2018/1/25.
 */

@RestController
@RequestMapping("/relationship")
public class RelationshipResource {

    @Autowired
    private RelationshipService relationshipService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/{memberId}/following", method = RequestMethod.GET, produces = "application/json")
    public Object getFollowing(@ModelAttribute("member") Member member,
                               @PathVariable("memberId") Long memberId,
                               @PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return relationshipService.getFollowingByMember(memberId, pageable);
    }

    @RequestMapping(value = "/{memberId}/fan", method = RequestMethod.GET, produces = "application/json")
    public Object getFan(@ModelAttribute("member") Member member,
                         @PathVariable("memberId") Long memberId,
                         @PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return relationshipService.getFanByMember(memberId, pageable);
    }

    @RequestMapping(value = "/{memberId}/friends" , method = RequestMethod.GET , produces = "application/json")
    public Object getFriends(@ModelAttribute("member")Member member,
                             @PathVariable("memberId") Long memberId,
                             @PageableDefault(value = 10) Pageable pageable){
            return relationshipService.getFriendsByMember(memberId,pageable);
    }


    @RequestMapping(value = "/blocking", method = RequestMethod.GET, produces = "application/json")
    public Object getBlocking(@ModelAttribute("member") Member member){
        return relationshipService.getBlockingByMember(member);
    }



    @RequestMapping(value = "/follow/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object followAMember(@ModelAttribute("member") Member member,
                                @PathVariable("memberId") Long followMemberId,
                                @RequestHeader("Accept-Language")String language) throws NotAcceptableException{
        try{
            relationshipService.followMember(member, followMemberId,language);
            return Http.standardResponse();
        } catch (PreconditionNotQualifiedException e) {
            return Http.standardResponse(ErrorCode.CANNOT_FOLLOW.getStatus(),ErrorCode.CANNOT_FOLLOW.getMsg());
        }

    }

    @RequestMapping(value = "/unfollow/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object unfollowAMember(@ModelAttribute("member") Member member,
                                @PathVariable("memberId") Long unfollowMemberId){
        relationshipService.unfollowMember(member, unfollowMemberId);
        return Http.standardResponse(null);
    }

    @RequestMapping(value = "/block/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object blockAMember(@ModelAttribute("member") Member member,
                             @PathVariable("memberId") Long blockMemberId) throws NotFoundException {
        relationshipService.blockMember(member, blockMemberId);
        return Http.standardResponse(null);
    }

    @RequestMapping(value = "/unblock/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object unblockAMember(@ModelAttribute("member") Member member,
                               @PathVariable("memberId") Long unblockMemberId){
        relationshipService.unblockMember(member, unblockMemberId);
        return Http.standardResponse(null);
    }



}
