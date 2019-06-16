package chat.hala.app.repository;

import chat.hala.app.entity.SystemNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SystemNoticeRepository  extends JpaRepository<SystemNotice,Long> {


    @Query(value = "select * from system_notice s where s.receive_id = :id order by s.is_read , s.time desc limit :page ,:size ",nativeQuery = true)
    List<SystemNotice> findNoticeList(@Param("id") Long id, @Param("page") int offset, @Param("size") int pageSize);

    @Modifying
    @Query(value = "update system_notice s set s.is_read=1 where s.receive_id = :memberId and s.time < :time",nativeQuery = true)
    void readNoticeList(Long memberId, Date time);
}
