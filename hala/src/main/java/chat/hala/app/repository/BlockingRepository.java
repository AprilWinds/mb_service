package chat.hala.app.repository;

import chat.hala.app.entity.Blocking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/1/25.
 */
public interface BlockingRepository extends JpaRepository<Blocking, Long> {
    public Blocking findByBlockerIdAndBlockingMemberId(Long blockerId, Long blockingMemberId);
    public List<Blocking> findByBlockerIdOrderByCreatedAtDesc(Long blockerId);

    @Query(value = "select * from blocking where (blocker_id = :m1Id and blocking_member_id = :m2Id) OR (blocker_id = :m2Id and blocking_member_id = :m1Id)", nativeQuery = true)
    public List<Blocking> findBlockingRelation(@Param("m1Id") Long m1Id, @Param("m2Id") Long m2Id);

}
