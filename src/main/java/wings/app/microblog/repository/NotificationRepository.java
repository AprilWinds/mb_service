package wings.app.microblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wings.app.microblog.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {



    @Query(value = "select n from Notification n where n.receiveId=:id")
    List<Notification> findNotificationList(@Param("id") Long id);
}
