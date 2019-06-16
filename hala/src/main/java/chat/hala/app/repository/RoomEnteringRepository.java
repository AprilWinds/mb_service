package chat.hala.app.repository;

import chat.hala.app.entity.RoomEntering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/1/27.
 */
public interface RoomEnteringRepository extends JpaRepository<RoomEntering, Long> {
    public List<RoomEntering> findByRoomIdOrderByCreatedAtAsc(Long roomId);

    @Query(value = "select * from room_entering where member_id = :memberId and room_id = :roomId and inorout = 1 order by created_at desc limit 1", nativeQuery = true)
    public RoomEntering findLastMemberEnter(@Param("memberId") Long memberId, @Param("roomId") Long roomId);

    @Query(value = "select * from room_entering where member_id = :memberId and room_id = :roomId order by created_at desc limit 1", nativeQuery = true)
    public RoomEntering findLastMemberEnterOrExit(@Param("memberId") Long memberId, @Param("roomId") Long roomId);
}
