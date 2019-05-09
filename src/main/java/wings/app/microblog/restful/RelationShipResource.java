package wings.app.microblog.restful;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import wings.app.microblog.Exception.BlockedException;
import wings.app.microblog.entity.Member;
import wings.app.microblog.service.RelationShipService;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.util.Http;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/relation")
public class RelationShipResource {

    @Autowired
    private RelationShipService relationShipService;

    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request) {
        return (Member) request.getAttribute("member");
    }


    @RequestMapping(value = "/follow/{memberId}",method = RequestMethod.POST,  produces = "application/json")
    public  Object follow(@ModelAttribute("member") Member member, @PathVariable("memberId")Long memberId){
        try {
            relationShipService.follow(member.getId(),memberId);
        } catch (BlockedException e) {
            e.printStackTrace();
           return Http.standardResponse(ErrorCode.BLOCKED.status,ErrorCode.BLOCKED.msg);
        }
        return Http.standardResponse();
    }

    @RequestMapping(value = "/unfollow/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object unfollowMember(@ModelAttribute("member") Member member,
                                  @PathVariable("memberId") Long memberId){
        relationShipService.unfollow(member.getId(), memberId);
        return Http.standardResponse();
    }
    @RequestMapping(value = "/block/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object blockAMember(@ModelAttribute("member") Member member,
                               @PathVariable("memberId") Long blockMemberId){
        relationShipService.block(member.getId(), blockMemberId);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/unblock/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public Object unblockAMember(@ModelAttribute("member") Member member,
                                 @PathVariable("memberId") Long unblockMemberId){
        relationShipService.unblock(member.getId(), unblockMemberId);
        return Http.standardResponse();
    }

    @RequestMapping(value = "/followList", method = RequestMethod.GET, produces = "application/json")
    public Object getFollowList(@ModelAttribute("member") Member member,  @PageableDefault(value = 10)Pageable pageable){
        return Http.standardResponse(relationShipService.getFollowList(member.getId(),pageable));
    }

    @RequestMapping(value = "/fansList", method = RequestMethod.GET, produces = "application/json")
    public Object getFansList(@ModelAttribute("member") Member member,  @PageableDefault(value = 10)Pageable pageable){
        return Http.standardResponse(relationShipService.getFansList(member,pageable));
    }

    @RequestMapping(value = "/blackList", method = RequestMethod.GET, produces = "application/json")
    public Object getBlackList(@ModelAttribute("member") Member member,  @PageableDefault(value = 10)Pageable pageable){
        return Http.standardResponse(relationShipService.getBlackList(member.getId(),pageable));
    }

    @RequestMapping(value="/{memberId}",method = RequestMethod.GET,produces = "application/json")
    public Object relation(@ModelAttribute("member")Member member,@PathVariable("memberId")Long mid) {
        Integer relation = relationShipService.getRelation(member, mid);
        return  Http.standardResponse(relation);
    }
}