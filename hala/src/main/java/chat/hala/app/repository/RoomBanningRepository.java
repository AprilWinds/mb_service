package chat.hala.app.repository;

import chat.hala.app.entity.RoomBanning;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/3/20.
 */
public interface RoomBanningRepository extends JpaRepository<RoomBanning, Long> {
}
