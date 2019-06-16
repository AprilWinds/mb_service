package chat.hala.app.repository;

import chat.hala.app.entity.MemberCheckinLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by astropete on 2018/2/24.
 */
public interface MemberCheckinLogRepository extends JpaRepository<MemberCheckinLog, Long> {
    @Query(value = "SELECT * FROM member_checkin_log WHERE member_id = :memberId AND created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY) AND created_at < CURRENT_DATE LIMIT 1", nativeQuery = true)
    public MemberCheckinLog findYesterdayCheckin(@Param("memberId") Long memberId);

    @Query(value = "SELECT * FROM member_checkin_log WHERE member_id = :memberId AND created_at >= CURRENT_DATE LIMIT 1", nativeQuery = true)
    public MemberCheckinLog findTodayCheckin(@Param("memberId") Long memberId);
}
