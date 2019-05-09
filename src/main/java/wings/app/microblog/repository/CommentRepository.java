package wings.app.microblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wings.app.microblog.entity.Comment;
import java.util.List;

public interface  CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "select c from Comment c where c.parentId=0 and c.momentId=:momentId order by c.time desc")
    List<Comment> findTopCommentList(@Param("momentId") Long momentId, Pageable pageable);

    @Query(value = "select c from Comment c where c.parentId=:id and c.momentId=:momentId order by c.time asc " )
    List<Comment> findByParentIdList(@Param("id") Long id, @Param("momentId") Long momentId);
}
