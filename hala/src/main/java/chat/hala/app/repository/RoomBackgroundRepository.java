package chat.hala.app.repository;

import chat.hala.app.entity.RoomBackground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RoomBackgroundRepository extends JpaRepository<RoomBackground,Long> {



    @Transactional
    @Modifying
    @Query(value = "insert into room_background(bg_type,avatar_url) values(:t,:url)",nativeQuery = true)
    void addRoomBackground(@Param("t") Integer type, @Param("url") String url);
}
