package chat.hala.app.service;

import chat.hala.app.entity.*;
import chat.hala.app.service.exception.ObjectNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by astropete on 2018/6/12.
 */
public interface WAService {

    public Object findMembersPage(Pageable pageable);

    public Object searchMembers(String keyword);

    public void activateOrDeMember(Long memberId, Member activated);

    public Object findRoomsPage(Pageable pageable);

    public Object searchRooms(String keyword);

    public void updateARoom(Long roomId, Room updated);

    public Object findFeedbacks(Pageable pageable);

    public Object findMaterials();

    public void deleteMaterial(Long materialId);

    public void addMaterial(AppMaterial material);

    public Object findReports(Pageable pageable);

    public void replyFeedback(Long fid, String reply) throws Exception;

    public WebAdmin loginWA(String name, String password);

    public WebAdmin validateToken(String token);

    public Object findAnalytics();

    public Object findRecharges(Pageable pageable);

    public void manualRecharge(Member m) throws ObjectNotFoundException;


    Object findApprovalsPageByState(Integer state, String type, Pageable pageable);

    void updateApprovalRejection(Long id, String description, String type);

    void updateApprovalPass(Long id, String type);

    void  addRoomBackgroundList(String type, String[] url);

    Object findRoomBackgrounds(Pageable pageable);

    Object findRoomStylePage(Pageable pageable);

    void deleteBackgrounds(List<Long> select);

    void addRoomStyle(RoomStyle roomStyle);

    void updateRoomStyle(RoomStyle roomStyle);

    void deleteRoomStyle(RoomStyle roomStyle);

    Object findTagsPage(Pageable pageable);

    void updateTag(Tag tag);

    void deleteTag(Long id);

    void addTag(Tag tag);

    Object findDynamicReports(Pageable pageable);

    void cancelDynamicReport(Long dynamicReportId);

    void affirmDynamicReport(Long dynamicId, Long dynamicReportId);

    Object fackMember(Member member);

    Object fackDynamic(Dynamic dynamic);
}
