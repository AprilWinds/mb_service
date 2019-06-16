package chat.hala.app.repository;

import chat.hala.app.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/3/4.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
    public Page<Feedback> findByRepliedFalse(Pageable pageable);
}
