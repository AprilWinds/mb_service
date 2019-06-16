package chat.hala.app.repository;

import chat.hala.app.entity.RoomFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/3/7.
 */
public interface RoomFollowerRepository extends JpaRepository<RoomFollower, Long> {
    public List<RoomFollower> findByMemberIdAndIsActiveTrue(Long memberId);
    public RoomFollower findByRoomIdAndMemberId(Long roomId, Long memberId);

    @Query(value = "select room_id from room_follower where member_id = :memberId and is_active = 1", nativeQuery = true)
    public List<Long> findRoomIdByMemberId(@Param("memberId") Long memberId);
}
