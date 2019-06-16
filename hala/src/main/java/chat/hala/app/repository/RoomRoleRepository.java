package chat.hala.app.repository;

import chat.hala.app.entity.RoomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/3/9.
 */
public interface RoomRoleRepository extends JpaRepository<RoomRole, Long> {
    public RoomRole findByRoomIdAndMemberIdAndRoleIsNot(Long roomId, Long memberId, RoomRole.MiR role);
    public RoomRole findByRoomIdAndMemberId(Long roomId, Long memberId);
    public List<RoomRole> findByRoomIdAndRole(Long roomId, RoomRole.MiR role);
    public List<RoomRole> findByRoomIdAndRoleIsNot(Long roomId, RoomRole.MiR role);
    public List<RoomRole> findByMemberId(Long memberId);

    @Query(value = "select * from room_role where room_id = :roomId order by field(role, 0, 1, 2), created_at asc", nativeQuery = true)
    public List<RoomRole> findByRoomIdOrderByRoleAndTimeAsc(@Param("roomId") Long roomId);

    @Query(value = "select distinct room_id from room_role where member_id = :memberId", nativeQuery = true)
    public List<Long> findRoomIdByMemberId(@Param("memberId") Long memberId);
}
