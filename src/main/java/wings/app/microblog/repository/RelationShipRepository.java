package wings.app.microblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.RelationShip;

import java.util.List;

@Repository
public interface RelationShipRepository extends JpaRepository<RelationShip,Long> {


    @Query(value = "select r from RelationShip r where (r.followerId= :a and r.followingId= :b) or (r.followerId= :b and r.followingId= :a)" )
    List<RelationShip> findByFromAndTo(@Param("a") Long a,@Param("b") Long b);

    @Query(value = "select r from  RelationShip r where r.followerId= :a and r.followingId=:b")
    RelationShip findByFollowing(@Param("a") Long a, @Param("b") Long b);

    @Query(value = "select m from RelationShip r,Member m where r.followerId=:id and r.followingId=m.id ")
    List<Member> findFollowList(@Param("id") Long id, Pageable pageable);

    @Query(value = "select m from RelationShip r,Member m where r.followingId=:id and r.followerId=m.id ")
    List<Member> findFansList(@Param("id") Long id, Pageable pageable);

    @Query(value = "select count(m) from RelationShip r,Member m where r.followerId=:id and r.followingId=m.id ")
    Integer findAllFollow(@Param("id") Long id);

    @Query(value = "select count(m) from RelationShip r,Member m where r.followingId=:id and r.followerId=m.id ")
    Integer findAllFans(@Param("id") Long id);

    @Query(value = "select * from relation_ship r where r.follower_id=:id or r.following_id=:id ",nativeQuery = true)
    List<RelationShip>  findByMemberId(@Param("id")Long id);
}
