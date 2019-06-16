package chat.hala.app.restful;

import chat.hala.app.entity.*;
import chat.hala.app.library.util.ErrorMsg;
import chat.hala.app.library.util.Http;
import chat.hala.app.restful.exception.*;
import chat.hala.app.restful.wrapper.MicrophoneSwitch;
import chat.hala.app.restful.wrapper.RoomWithLocate;
import chat.hala.app.service.ApprovalService;
import chat.hala.app.service.RoomService;
import chat.hala.app.service.exception.InvalidCredentialException;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.ObjectNotFoundException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by astropete on 2018/1/27.
 */

@RestController
@RequestMapping("/room")
public class RoomResource {

    @Autowired
    private RoomService roomService;


    @Autowired
    private ApprovalService approvalService;


    @ModelAttribute("member")
    public Member getUserFromToken(HttpServletRequest request)
    {
        return (Member) request.getAttribute("member");
    }

    @RequestMapping(value = "/latest", method = RequestMethod.GET, produces = "application/json")
    public Object getPopularRoom(@ModelAttribute("member") Member member,
                                 @PageableDefault(value = 10, sort = {"createdAt","attendersCount"}, direction = Sort.Direction.DESC) Pageable pageable){
        return roomService.getLatestRooms(member, pageable);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.GET, produces = "application/json")
    public Object getFollowedRoom(@ModelAttribute("member") Member member,
                                  @PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return roomService.getFollowedRooms(member, pageable);
    }

    @RequestMapping(value = "/nearby", method = RequestMethod.GET, produces = "application/json")
    public Object getNearbyRooms(@ModelAttribute("member") Member member,
                                 @RequestParam(value = "lng") Double longitude,
                                 @RequestParam(value = "lat") Double latitude,
                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        return roomService.getNearbyRooms(member, longitude, latitude, page, size);
    }

    @RequestMapping(value = "/explore", method = RequestMethod.GET, produces = "application/json")
    public Object getExploreRooms(@ModelAttribute("member") Member member,
                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                  @RequestParam(value = "country", required = false) String country){
        return roomService.getExploreRooms(country, page, size);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public Object createARoom(@ModelAttribute("member") Member member,
                              @RequestBody RoomWithLocate roomWithLocate,
                              HttpServletRequest request) throws PreconditionFailedException{
        try{
            String language = request.getHeader("Accept-Language");
            HashMap<String,Object> map = (HashMap<String, Object>) roomService.createRoom(member, roomWithLocate.getRoom(), roomWithLocate.getPosition(), language);
            approvalService.insertApproval("room",((Room)map.get("entity")).getId());

            return map;
        } catch (PreconditionNotQualifiedException e){
            throw new PreconditionFailedException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}", method = RequestMethod.GET, produces = "application/json")
    public Object getRoom(@ModelAttribute("member") Member member,
                          @PathVariable("roomId") Long roomId){
        return roomService.getRoomById(member, roomId);
    }

    @RequestMapping(value = "/{roomId}/microphone", method = RequestMethod.GET, produces = "application/json")
    public Object getRoomMics(@ModelAttribute("member") Member member,
                              @PathVariable("roomId") Long roomId) throws NotFoundException{
        try{
            return roomService.getRoomMics(roomId);
        } catch (ObjectNotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}/attender", method = RequestMethod.GET, produces = "application/json")
    public Object getRoomAttenders(@ModelAttribute("member") Member member,
                                   @PathVariable("roomId") Long roomId,
                                   @RequestParam(value = "excludes", required = false) String excludes,
                                   @PageableDefault(value = 10, sort = {"wealth", "createdAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        return roomService.getRoomAttenders(roomId, excludes, pageable);
    }

    @RequestMapping(value = "/{roomId}/member", method = RequestMethod.GET, produces = "application/json")
    public Object getRoomInsiders(@ModelAttribute("member") Member member,
                                  @PathVariable("roomId") Long roomId,
                                  @PageableDefault(value = 10, sort = {"createdAt"}, direction = Sort.Direction.ASC) Pageable pageable){
        return roomService.getRoomMembers(roomId, pageable);
    }

    @RequestMapping(value = "/{roomId}/member", method = RequestMethod.POST, produces = "application/json")
    public Object becomeRoomInsider(@ModelAttribute("member") Member member,
                                    @PathVariable("roomId") Long roomId) throws NotAcceptableException{
        try{
            return roomService.becomeARoomInsider(member, roomId);
        } catch (PreconditionNotQualifiedException e){
            throw new NotAcceptableException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}/member/rm", method = RequestMethod.POST, produces = "application/json")
    public Object revokeRoomInsider(@ModelAttribute("member") Member member,
                                   @PathVariable("roomId") Long roomId) {
        roomService.revokeARoomInsider(member, roomId);
        return Http.generalResponse();
    }

    @RequestMapping(value = "/{roomId}/follow", method = RequestMethod.POST, produces = "application/json")
    public Object followARoom(@ModelAttribute("member") Member member,
                              @PathVariable("roomId") Long roomId){
        roomService.followARoom(member, roomId);
        return Http.generalResponse();
    }

    @RequestMapping(value = "/{roomId}/unfollow", method = RequestMethod.POST, produces = "application/json")
    public Object unfollowARoom(@ModelAttribute("member") Member member,
                                @PathVariable("roomId") Long roomId){
        roomService.unfollowARoom(member, roomId);
        return Http.generalResponse();
    }

    @RequestMapping(value = "/{roomId}/microphone", method = RequestMethod.POST, produces = "application/json")
    public Object onOrOffMicrophone(@ModelAttribute("member") Member member,
                                    @PathVariable("roomId") Long roomId,
                                    @RequestBody RoomMicrophoneHolding holding) throws ConflictException, NotAcceptableException{
        try{
            return roomService.onOrOffMicrophone(member, roomId, holding);
        } catch (PreconditionNotQualifiedException e){
            throw new NotAcceptableException(e.getMessage());
        } catch (ObjectAlreadyExistedException e){
            throw new ConflictException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}/microphone/switch", method = RequestMethod.POST, produces = "application/json")
    public Object switchMicrophone(@ModelAttribute("member") Member member,
                                   @PathVariable("roomId") Long roomId,
                                   @RequestBody MicrophoneSwitch switching) throws ForbiddenException, NotAcceptableException{
        try{
            roomService.switchMicrophone(member, roomId, switching);
            return Http.generalResponse();
        } catch (InvalidCredentialException e){
            throw new ForbiddenException(e.getMessage());
        } catch (PreconditionNotQualifiedException e){
            throw new NotAcceptableException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}/inout", method = RequestMethod.POST, produces = "application/json")
    public Object enterOrLeaveARoom(@ModelAttribute("member") Member member,
                                    @PathVariable("roomId") Long roomId,
                                    @RequestBody RoomEntering entering) throws OutboundException, PreconditionFailedException{
        try{
            roomService.enterOrLeaveRoom(member, roomId, entering);
            return Http.generalResponse();
        } catch (PreconditionNotQualifiedException e){
            throw new PreconditionFailedException(e.getMessage());
        } catch (Exception e){
            throw new OutboundException(ErrorMsg.RONG_CLOUD_ERR);
        }
    }


    @RequestMapping(value = "/admin", method = RequestMethod.POST, produces = "application/json")
    public Object addOrKickRoomAdmin(@ModelAttribute("member") Member member,
                                     @RequestBody RoomRole role,
                                     @RequestParam("grant") Boolean grant) throws ForbiddenException, PreconditionFailedException{
        try{
            roomService.addOrKickAAdmin(member, role, grant);
            return Http.generalResponse();
        } catch (InvalidCredentialException e){
            throw new ForbiddenException(e.getMessage());
        } catch (PreconditionNotQualifiedException e){
            throw new PreconditionFailedException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}/adminids", method = RequestMethod.GET, produces = "application/json")
    public Object getRoomAdminIds(@ModelAttribute("member") Member member,
                                  @PathVariable("roomId") Long roomId){
        return roomService.getRoomAdminIds(roomId);
    }

    @RequestMapping(value = "/{roomId}/ban", method = RequestMethod.POST, produces = "application/json")
    public Object banActingInRoom(@ModelAttribute("member") Member member,
                                @RequestBody RoomBanning banning,
                                @PathVariable("roomId") Long roomId) throws OutboundException, ForbiddenException{
        try{
            roomService.banActingInRoom(member, roomId, banning);
            return Http.generalResponse();
        } catch (InvalidCredentialException e){
            throw new ForbiddenException(e.getMessage());
        } catch (Exception e){
            throw new OutboundException(ErrorMsg.RONG_CLOUD_ERR);
        }
    }

    @RequestMapping(value = "/{roomId}", method = RequestMethod.PUT, produces = "application/json")
    public Object editARoom(@ModelAttribute("member") Member member,
                            @RequestBody Room room,
                            @PathVariable("roomId") Long roomId) throws ForbiddenException{
        try {
            roomService.updateARoom(member, room, roomId);
            return Http.generalResponse();
        } catch (InvalidCredentialException e){
            throw new ForbiddenException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{roomId}/upgrade", method = RequestMethod.PUT, produces = "application/json")
    public Object upgradeARoom(@ModelAttribute("member") Member member,
                               @RequestBody Room room,
                               @PathVariable("roomId") Long roomId) throws ForbiddenException, PreconditionFailedException{


        try {
            return roomService.upgradeARoom(member, room, roomId);
        } catch (InvalidCredentialException e){
            throw new ForbiddenException(e.getMessage());
        } catch (PreconditionNotQualifiedException e){
            throw new PreconditionFailedException(e.getMessage());
        }
    }

    @RequestMapping(value = "/related/{memberId}", method = RequestMethod.GET, produces = "application/json")
    public Object getMemberRooms(@ModelAttribute("member") Member member,
                                 @PathVariable("memberId") Long requestMemberId){

        return roomService.getRoomsByMember(member, requestMemberId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public Object searchARoom(@ModelAttribute("member") Member member,
                              @RequestParam("keyword") String keyword,
                              @PageableDefault(value = 10, sort = {"attendersCount"}, direction = Sort.Direction.DESC) Pageable pageable){
        return roomService.searchRoomByKeyword(keyword, pageable);
    }

    @RequestMapping(value = "/{roomId}/remove", method = RequestMethod.POST, produces = "application/json")
    public Object removeARoom(@ModelAttribute("member") Member member,
                              @PathVariable("roomId") Long roomId) throws ForbiddenException{
        try{
            roomService.removeARoom(member, roomId);
            return Http.generalResponse();
        } catch (InvalidCredentialException e){
            throw new ForbiddenException(e.getMessage());
        }
    }


    @RequestMapping(value = "/style",method = RequestMethod.POST,produces = "application/json")
    public Object getRoomStyle(){
        return  null;
    }


}
