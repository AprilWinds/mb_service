package wings.app.microblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.BlackList;
import wings.app.microblog.entity.Member;

import java.util.List;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {


    @Query(value = "select b from BlackList b where (b.blockingId= :a and b.blockerId= :b) or (b.blockingId= :b and b.blockerId= :a)" )
    List<BlackList> findByFromAndTo(@Param("a") Long a, @Param("b") Long b);

    @Query(value = "select b from BlackList b where b.blockerId=:from and b.blockingId=:to")
    BlackList findByBlockingId(@Param("from") Long from,@Param("to") Long to);

    @Query(value = "select m from BlackList b,Member m where b.blockerId=:id and b.blockingId=m.id")
    List<Member> findBlackList(@Param("id") Long id, Pageable pageable);
}
