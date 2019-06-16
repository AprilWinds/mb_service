package chat.hala.app.restful;

import chat.hala.app.entity.Dynamic;
import chat.hala.app.entity.DynamicReport;
import chat.hala.app.entity.Member;
import chat.hala.app.library.util.ErrorCode;
import chat.hala.app.library.util.Http;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.restful.exception.PreconditionFailedException;
import chat.hala.app.restful.wrapper.CommentWithLocate;
import chat.hala.app.restful.wrapper.DynamicWithLocate;
import chat.hala.app.service.DynamicService;
import chat.hala.app.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/dynamic")
public class DynamicResource {


    @Autowired
    private DynamicService dynamicService;



    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request) {
        return (Member) request.getAttribute("member");
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public Object createDynamic(@ModelAttribute("member") Member member,
                                @RequestBody DynamicWithLocate dynamicWithLocate,
                                @RequestHeader("Accept-Language")String language) throws PreconditionFailedException {
        try {
            return dynamicService.addDynamic(member, dynamicWithLocate,language);
        } catch (PreconditionFailedException e) {
            return  Http.standardResponse(ErrorCode.No_IMG_CONTENT.getStatus(),ErrorCode.No_IMG_CONTENT.getMsg());
        }

    }

    @RequestMapping(value = "/follow", method = RequestMethod.GET, produces = "application/json")
    public Object getFollowDynamic(@ModelAttribute("member") Member member,
                                    @PageableDefault(value = 10)Pageable  pageable){
        return dynamicService.findDynamicByFollow(member.getId(),pageable);
    }


    @RequestMapping(value = "/nearby", method = RequestMethod.GET, produces = "application/json")
    public Object getNearByDynamic(@ModelAttribute("member")Member member,
                                   @RequestParam("lng") Double lng,
                                   @RequestParam("lat") Double lat,
                @PageableDefault(value = 10)Pageable pageable){
        return  dynamicService.findNearbyDynamic(member.getId(),lng, lat, pageable);
    }


    @RequestMapping(value ="/{dynamicId}/like",method = RequestMethod.POST,produces = "application/json")
    public Object createStar(@ModelAttribute("member")Member member,
                             @PathVariable("dynamicId")Long dynamicId,@RequestHeader("Accept-Language")String language) throws NotFoundException{
        try {
            return dynamicService.star(member.getId(),dynamicId,language);
        } catch (ObjectNotFoundException e) {
            return  Http.standardResponse(ErrorCode.NO_DYNAMIC.getStatus(),ErrorCode.NO_DYNAMIC.getMsg());
        }
    }

  /*  @RequestMapping(value = "/{dynamicId}/dislike",method = RequestMethod.POST,produces = "application/json")
    public Object cancelStar(@ModelAttribute("member")Member member,
                             @PathVariable("dynamicId")Long dynamicId) throws NotFoundException, ConflictException {
        try {
            return  dynamicService.cancelStar(member.getId(),dynamicId);
        } catch (ObjectNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ObjectAlreadyUpdateException e) {
            throw new ConflictException(e.getMessage());
        }
    }*/


    @RequestMapping(value = "/addComment",method = RequestMethod.POST,produces = "application/json")
    public  Object createComment(@ModelAttribute("member")Member member,@RequestBody CommentWithLocate commentWithLocate
            ,@RequestHeader("Accept-Language")String language) throws NotFoundException {
        try {
            return dynamicService.addComment(member,commentWithLocate,language);
        } catch (ObjectNotFoundException e) {
            return  Http.standardResponse(ErrorCode.NO_DYNAMIC.getStatus(),ErrorCode.NO_DYNAMIC.getMsg());
        }
    }

    @RequestMapping(value = "/{dynamicId}/comments",method =RequestMethod.GET,produces = "application/json")
    public  Object getPageComments(@PathVariable("dynamicId")Long dynamicId,
                                  @RequestParam("lng")Double lng,
                                  @RequestParam("lat")Double lat,
                                  @RequestParam(value = "page",required = false,defaultValue ="0")Integer page,
                                  @RequestParam(value = "size",required = false,defaultValue ="10")Integer size, @ModelAttribute("member")Member member) throws NotFoundException {
        try {
            return  dynamicService.findPageComments(member,dynamicId,page,size,lat,lng);
        } catch (ObjectNotFoundException e) {
            return  Http.standardResponse(ErrorCode.NO_DYNAMIC.getStatus(),ErrorCode.NO_DYNAMIC.getMsg());
        }
    }


    @RequestMapping(value = "/{dynamicId}",method = RequestMethod.GET,produces = "application/json")
    public  Object getDynamicById(@PathVariable("dynamicId")Long dynamicId,@ModelAttribute("member")Member member){
        return dynamicService.findDynamicById(dynamicId,member);
    }

    @RequestMapping(value = "/report",method = RequestMethod.POST, produces = "application/json")
    public  Object addDynamicReport( @ModelAttribute("member")Member member, @RequestBody DynamicReport dynamicReport){
        try {
            return  dynamicService.addDynamicReport(member,dynamicReport);
        } catch (ObjectNotFoundException e) {
            return  Http.standardResponse(ErrorCode.NO_DYNAMIC.getStatus(),ErrorCode.NO_DYNAMIC.getMsg());
        }
    }


    @RequestMapping(value = "/{dynamicId}/privilege", method = RequestMethod.POST, produces = "application/json")
    public Object  updateDynamicPrivilege(@RequestParam("type") Dynamic.Type type, @ModelAttribute("member")Member member, @PathVariable("dynamicId")Long dynamicId){
        try {
            return  dynamicService.updateDynamicPrivilege(dynamicId,member,type);
        } catch (Exception e) {
            return  Http.standardResponse(ErrorCode.NO_DYNAMIC.getStatus(),ErrorCode.NO_DYNAMIC.getMsg());
        }
    }
/*
    @RequestMapping(value = "/{dynamicId}",method = RequestMethod.DELETE, produces = "application/json")
    public  Object hideDynamic(@ModelAttribute("member")Member member,@PathVariable("dynamicId")Long dynamicId){
        try {
            return  dynamicService.hideDynamic(member , dynamicId);
        } catch (Exception e) {
            return Http.standardResponse(ErrorCode.NO_DYNAMIC.getStatus(),ErrorCode.NO_DYNAMIC.getMsg());
        }
    }*/
}