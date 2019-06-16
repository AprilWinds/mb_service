package chat.hala.app.repository;

import chat.hala.app.entity.MemberTaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by astropete on 2018/3/7.
 */
public interface MemberTaskLogRepository extends JpaRepository<MemberTaskLog, Long> {

    @Query(value = "select task_id from member_task_log where member_id = :memberId and task_id in (:taskIds)", nativeQuery = true)
    public List<BigInteger> findMemberCompletedTaskIds(@Param("memberId") Long memberId, @Param("taskIds") List<Long> taskIds);

    @Query(value = "select task_id from member_task_log where member_id = :memberId and task_id in (:taskIds) and created_at >= :startDate", nativeQuery = true)
    public List<BigInteger> findMemberCompletedDailyTaskIds(@Param("memberId") Long memberId, @Param("taskIds") List<Long> taskIds, @Param("startDate") Date startDate);

    @Query(value = "select task_id from member_task_log where created_at > current_date and task_id in (select id from task where is_daily = 1) and member_id = :memberId", nativeQuery = true)
    public List<BigInteger> findMemberCompletedDailyTaskIds(@Param("memberId") Long memberId);

    @Query(value = "select task_id from member_task_log where task_id in (select id from task where is_daily = 1) and member_id = :memberId", nativeQuery = true)
    public List<BigInteger> findMemberCompletedNonDailyTaskIds(@Param("memberId") Long memberId);

    public MemberTaskLog findByMemberIdAndTaskIdAndCreatedAtAfter(Long memberId, Long taskId, Date d);
    public MemberTaskLog findByMemberIdAndTaskId(Long memberId, Long taskId);
    public MemberTaskLog findTop1ByMemberIdAndTaskIdOrderByCreatedAtDesc(Long memberId, Long taskId);
}
