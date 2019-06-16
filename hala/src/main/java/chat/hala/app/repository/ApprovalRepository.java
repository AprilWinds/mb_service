package chat.hala.app.repository;

import chat.hala.app.entity.Approval;
import chat.hala.app.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval,Long>{



    @Transactional
    @Modifying
    @Query(value = "update approval a set a.state=2 , a.audit_count=a.audit_count+1 where a.member_id= :memberId",nativeQuery = true)
    void updateMemberApprovalPass(@Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query(value = "update approval a set a.state=1,a.audit_count=a.audit_count+1,a.description= :description where a.member_id= :memberId",nativeQuery = true)
    void updateMemberApprovalRejection(@Param("memberId") Long memberId, @Param("description") String description);


    @Transactional
    @Modifying
    @Query(value = "update approval a set a.state=2 , a.audit_count=a.audit_count+1 where a.room_id= :roomId",nativeQuery = true)
    void updateRoomApprovalPass(@Param("roomId") Long roomId);

    @Transactional
    @Modifying
    @Query(value = "update approval a set a.state=1,a.audit_count=a.audit_count+1,a.description= :description where a.room_id= :roomId",nativeQuery = true)
    void updateRoomApprovalRejection(@Param("roomId") Long roomId, @Param("description") String description);



    @Query(value = "select new chat.hala.app.entity.Approval(a.id,a.memberId,a.state,a.auditCount,a.description) from  Approval  a where a.state= :state and a.roomId=0 ")
    List<Approval> findAllByMemberAndState(@Param("state")Integer state,Pageable pageable);


   @Query(value = "select new chat.hala.app.entity.Approval(ap.id,ap.roomId,ap.state,ap.auditCount,ap.description,ap.memberId) from Approval ap where ap.state = :state and ap.memberId=0 ")
    List<Approval> findAllByRoomAndState(@Param("state")Integer state,Pageable pageable);

   @Transactional
   @Modifying
   @Query(value = "insert  into approval(member_id) values(:memberId)",nativeQuery = true)
   void  insertApprovalByMember(@Param("memberId")Long memberId);

   @Transactional
   @Modifying
   @Query(value = "insert into  approval(room_id) values(:roomId)",nativeQuery = true)
   void insertApprovalByRoom(@Param("roomId")Long roomId);


   @Query(value = "select * from approval a  where a.member_id = :memberId",nativeQuery = true)
   Approval findMemberByState(@Param("memberId") Long memberId);


   @Query(value = "select * from approval a where a.room_id = :roomId",nativeQuery = true)
   Approval findRoomByState(@Param("roomId") Long roomId);

}