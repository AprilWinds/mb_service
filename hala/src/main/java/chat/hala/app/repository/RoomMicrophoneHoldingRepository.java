package chat.hala.app.repository;

import chat.hala.app.entity.RoomMicrophoneHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/1/27.
 */
public interface RoomMicrophoneHoldingRepository extends JpaRepository<RoomMicrophoneHolding, Long> {
    public RoomMicrophoneHolding findTop1ByRoomIdAndMemberIdOrderByCreatedAtDesc(Long roomId, Long memberId);

    @Query(value = "SELECT * FROM room_microphone_holding WHERE room_id = :roomId AND onoff = 1 ORDER BY microphone_number ASC", nativeQuery = true)
    public List<RoomMicrophoneHolding> findMicrophoneHoldings(@Param("roomId") Long roomId);
}
