package wings.app.microblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wings.app.microblog.entity.Feedback;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback,Long> {


    @Query(value = "select f from Feedback f order by f.time desc")
    List<Feedback> findByPage(Pageable pageable);
}
