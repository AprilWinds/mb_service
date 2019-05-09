package wings.app.microblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.Msg;

import java.util.List;

@Repository
public interface MsgRepository extends JpaRepository<Msg,Long> {


    @Query(value = "select * from msg m where m.to_id=:id order by m.time desc, m.from_id",nativeQuery = true)
    List<Msg> findByOffLine(@Param("id") Long id);
}
