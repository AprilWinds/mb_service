package chat.hala.app.restful;

import chat.hala.app.entity.*;
import chat.hala.app.library.util.Constant;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.restful.exception.ForbiddenException;
import chat.hala.app.restful.exception.PreconditionFailedException;
import chat.hala.app.service.WAService;
import chat.hala.app.service.exception.InvalidCredentialException;
import chat.hala.app.service.exception.ObjectNotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * Created by astropete on 2018/6/12.
 */


@RestController
@RequestMapping("/was")
public class AdminResource {

    @Autowired
    private WAService waService;

    @ModelAttribute("wa")
    public WebAdmin getWAFromToken(HttpServletRequest request) throws Exception
    {
        if(request.getRequestURI().contains("login")) return null;
        WebAdmin wa = (WebAdmin) request.getAttribute("wa");
        if(wa == null) throw new InvalidCredentialException(ErrorMsg.NO_MEMBER);
        if(wa.getRole().equals(WebAdmin.WARole.customerservice) && !(request.getRequestURI().contains("feedback") || request.getRequestURI().contains("report")))
            throw new ForbiddenException(ErrorMsg.NO_PERMISSION);
        return wa;
    }


    @RequestMapping(value = "/member", method = RequestMethod.GET, produces = "application/json")
    public Object findMembersPage(@PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return waService.findMembersPage(pageable);
    }

    @RequestMapping(value = "/member/search", method = RequestMethod.GET, produces = "application/json")
    public Object searchMembersByKey(@RequestParam("keyword") String keyword){
        return waService.searchMembers(keyword);
    }

    @RequestMapping(value = "/member/{memberId}/activate", method = RequestMethod.POST, produces = "application/json")
    public void activateOrDeMember(@PathVariable("memberId") Long memberId,
                                   @RequestBody Member activated){
        waService.activateOrDeMember(memberId, activated);
    }

    @RequestMapping(value = "/room", method = RequestMethod.GET, produces = "application/json")
    public Object findRoomsPage(@PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return waService.findRoomsPage(pageable);
    }

    @RequestMapping(value = "/room/search", method = RequestMethod.GET, produces = "application/json")
    public Object searchRoomsByKey(@RequestParam("keyword") String keyword){
        return waService.searchRooms(keyword);
    }

    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.POST, produces = "application/json")
    public void updateARoom(@PathVariable("roomId") Long roomId,
                            @RequestBody Room room){
        waService.updateARoom(roomId, room);
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.GET, produces = "application/json")
    public Object findFeedbacks(@PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return waService.findFeedbacks(pageable);
    }

    @RequestMapping(value = "/material", method = RequestMethod.GET, produces = "application/json")
    public Object findMaterials(){
        return waService.findMaterials();
    }

    @RequestMapping(value = "/material/{materialId}/rm", method = RequestMethod.POST, produces = "application/json")
    public void deleteMaterial(@PathVariable("materialId") Long materialId){
        waService.deleteMaterial(materialId);
    }

    @RequestMapping(value = "/material", method = RequestMethod.POST, produces = "application/json")
    public void addMaterial(@RequestBody AppMaterial material){
        waService.addMaterial(material);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET, produces = "application/json")
    public Object getReport(@PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return waService.findReports(pageable);
    }

    @RequestMapping(value = "/dynamicReport",method = RequestMethod.GET,produces = "application/json")
    public Object getDynamicReport(@PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return  waService.findDynamicReports(pageable);
    }

    @RequestMapping(value = "/dynamicReport/affirm",method = RequestMethod.PUT,produces = "application/json")
    public void affirmDynamicReport(@RequestParam("dynamicId")Long dynamicId,@RequestParam("id")Long dynamicReportId){
         waService. affirmDynamicReport(dynamicId,dynamicReportId);
    }

    @RequestMapping(value = "/dynamicReport/cancel",method = RequestMethod.PUT,produces="application/json")
    public void cancelDynamicReport(@RequestParam("id")Long dynamicReportId){
         waService.cancelDynamicReport(dynamicReportId);
    }

    @RequestMapping(value = "/feedback/{feedbackId}/reply", method = RequestMethod.POST, produces = "application/json")
    public void replyFeedback(@PathVariable("feedbackId") Long fid,
                                @RequestBody Reply reply) throws Exception{
        waService.replyFeedback(fid, reply.getReply());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public Object loginWA(@RequestParam("admin") String admin,
                          @RequestParam("password") String password){
        WebAdmin wa = waService.loginWA(admin, password);
        if(wa == null) return null;
        else return Jwts.builder().setSubject(String.valueOf(wa.getId())).setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, Constant.getPrivateKey()).compact();
    }

    @RequestMapping(value = "/analyze", method = RequestMethod.GET, produces = "application/json")
    public Object analyze(){
        return waService.findAnalytics();
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.GET, produces = "application/json")
    public Object getRecharge(@PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return waService.findRecharges(pageable);
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.POST, produces = "application/json")
    public void getRecharge(@RequestBody Member tmpMember) throws PreconditionFailedException{
        try{
            waService.manualRecharge(tmpMember);
        } catch (ObjectNotFoundException e) {
            throw new PreconditionFailedException(e.getMessage());
        }
    }


    @RequestMapping(value = "/approvals",method = RequestMethod.GET,produces = "application/json")
    public Object  findApprovalsPage(@PageableDefault(value = 10,sort = {"auditCount"},direction = Sort.Direction.ASC) Pageable pageable,Integer state,String type) {

        return  waService.findApprovalsPageByState(state,type,pageable);
    }

    @RequestMapping(value = "/updateApprovalPass/{id}",method =RequestMethod.PUT )
    public  void updateApprovalPass(@PathVariable("id")Long id,  @RequestBody String type){
        waService.updateApprovalPass(id,type);
    }

    @RequestMapping(value = "/updateApprovalRejection/{id}",method = RequestMethod.PUT)
    public void updateApprovalRejection(@PathVariable("id")Long id, @RequestParam String description,@RequestParam String type) {

        waService.updateApprovalRejection(id,description,type);
    }


    @RequestMapping(value ="/addRoomBackground",method = RequestMethod.POST,produces = "application/json")
    public void addRoomBackground(@RequestBody String[] strings , String type) {
        waService.addRoomBackgroundList(type, strings);
    }


    @RequestMapping(value = "/roomBackgrounds",method = RequestMethod.GET,produces = "application/json")
    public  Object findRoomBackgrounds(@PageableDefault(value = 16,sort = {"id"},direction =Sort.Direction.DESC)Pageable pageable){
        return  waService.findRoomBackgrounds(pageable);
    }


    @RequestMapping(value = "/roomStyles",method = RequestMethod.GET,produces = "application/json")
    public  Object findRoomStyles(@PageableDefault(value = 10,sort = {"id"},direction = Sort.Direction.ASC)Pageable pageable){
        return  waService.findRoomStylePage(pageable);
    }

    @RequestMapping(value = "/deleteRoomBackground",method = RequestMethod.POST,produces = "application/json")
    public void deleteRoomBackground(@RequestBody List<Long> select){
            waService.deleteBackgrounds(select);
    }


    @RequestMapping(value = "/addRoomStyle",method = RequestMethod.POST,produces = "application/json")
    public  void  addRoomStyle(@RequestBody RoomStyle roomStyle){
        waService.addRoomStyle(roomStyle);
    }

    @RequestMapping(value = "/updateRoomStyle",method = RequestMethod.PUT,produces = "application/json")
    public  void  updateRoomStyle(@RequestBody RoomStyle roomStyle){
        waService.updateRoomStyle(roomStyle);
    }

    @RequestMapping(value = "/deleteRoomStyle",method = RequestMethod.POST,produces = "application/json")
    public  void  deleteRoomStyle(@RequestBody RoomStyle roomStyle){
        waService.deleteRoomStyle(roomStyle);
    }


    @RequestMapping(value = "/tags",method = RequestMethod.GET,produces = "application/json")
    public Object  findTags(@PageableDefault(size = 10)Pageable pageable){
        return   waService.findTagsPage(pageable);
    }

    @RequestMapping(value = "/updateTag",method = RequestMethod.PUT,produces = "application/json")
    public  void  updateTag(@RequestBody Tag tag){
         waService.updateTag(tag);
    }

    @RequestMapping(value = "/deleteTag/{id}",method = RequestMethod.DELETE,produces = "application/json")
    public  void  deleteTag(@PathVariable("id")Long id){
        waService.deleteTag(id);
    }

    @RequestMapping(value ="/addTag",method = RequestMethod.POST,produces = "application/json")
    public  void  addTag(@RequestBody Tag tag){waService.addTag(tag);}

    @RequestMapping(value ="/member/fack",method = RequestMethod.POST,produces = "application/json")
    public Object fackMember(@RequestBody Member member){
        return   waService.fackMember(member);
    }



    @RequestMapping(value ="/dynamic/fack",method = RequestMethod.POST,produces = "application/json")
    public Object fackDynamic(@RequestBody Dynamic dynamic){
        return   waService.fackDynamic(dynamic);
    }
}



class Reply {
    private String reply;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}