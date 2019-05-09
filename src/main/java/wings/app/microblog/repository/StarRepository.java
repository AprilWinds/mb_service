package wings.app.microblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.Star;

@Repository
public interface StarRepository  extends JpaRepository<Star,Long> {

    @Query("select s from Star s where s.momentId=:momentId and s.fromId=:memberId")
    Star findByPriseMemberIdAndMomentId(@Param("memberId") Long memberId, @Param("momentId") Long momentId);

    @Query("select count(s) from Star s where s.fromId=:fromId and s.momentId=:momentId and s.isDelete=0")
    Integer findIsPrise(@Param("fromId") Long fromId,@Param("momentId")Long momentId);
}
