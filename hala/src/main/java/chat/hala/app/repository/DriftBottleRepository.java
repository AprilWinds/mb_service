package chat.hala.app.repository;

import chat.hala.app.entity.DriftBottle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DriftBottleRepository extends JpaRepository<DriftBottle,Long> {


    @Query(value = "select * from drift_bottle d where d.seneder_id != :id and d.salvage_id is NULL ",nativeQuery = true)
    DriftBottle getNotMeBottle(Long id);
}
