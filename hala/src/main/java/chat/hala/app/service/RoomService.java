package chat.hala.app.service;

import chat.hala.app.entity.*;
import chat.hala.app.restful.wrapper.MemberInRoom;
import chat.hala.app.restful.wrapper.MemberOnMic;
import chat.hala.app.restful.wrapper.MicrophoneSwitch;
import chat.hala.app.service.exception.InvalidCredentialException;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.ObjectNotFoundException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import com.vividsolutions.jts.geom.Coordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by astropete on 2018/1/28.
 */
public interface RoomService {
    public Object createRoom(Member member, Room room, Coordinate locate, String language) throws PreconditionNotQualifiedException;
    public Room getRoomById(Member member, Long roomId);
    public void enterOrLeaveRoom(Member member, Long roomId, RoomEntering entering) throws Exception, PreconditionNotQualifiedException;
    public void addOrKickAAdmin(Member member, RoomRole role, Boolean grant) throws InvalidCredentialException, PreconditionNotQualifiedException;
    public void banActingInRoom(Member member, Long roomId, RoomBanning banning) throws Exception, InvalidCredentialException;
    public void updateARoom(Member member, Room room, Long roomId) throws InvalidCredentialException;
    public Object upgradeARoom(Member member, Room room, Long roomId) throws InvalidCredentialException, PreconditionNotQualifiedException;
    public Object onOrOffMicrophone(Member member, Long roomId, RoomMicrophoneHolding holding) throws ObjectAlreadyExistedException, PreconditionNotQualifiedException;
    public void switchMicrophone(Member member, Long roomId, MicrophoneSwitch switching) throws InvalidCredentialException, PreconditionNotQualifiedException;
    public List<MemberOnMic> getRoomMics(Long roomId) throws ObjectNotFoundException;
    public List<MemberInRoom> getRoomAttenders(Long roomId, String excludes, Pageable pageable);
    public List<Member> getRoomMembers(Long roomId, Pageable pageable);
    public List<Room> getLatestRooms(Member member, Pageable pageable);
    public Page<Room> getFollowedRooms(Member member, Pageable pageable);
    public List<Room> getNearbyRooms(Member member, Double longitude, Double latitude, Integer page, Integer size);
    public List<Room> getExploreRooms(String country, Integer page, Integer size);
    public Object getRoomsByMember(Member member, Long requestMemberId);
    public Page<Room> searchRoomByKeyword(String keyword, Pageable pageable);
    public Object becomeARoomInsider(Member member, Long roomId) throws PreconditionNotQualifiedException;
    public void revokeARoomInsider(Member member, Long roomId);
    public void followARoom(Member member, Long roomId);
    public void unfollowARoom(Member member, Long roomId);
    public List<Long> getRoomAdminIds(Long roomId);
    public void removeARoom(Member member, Long roomId) throws InvalidCredentialException;
}
