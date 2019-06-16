package chat.hala.app.repository;

import chat.hala.app.entity.Relationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/1/25.
 */
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    public Relationship findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query(value = "select count(id) from relationship where follower_id = :followerId", nativeQuery = true)
    public Integer findCountOfFollowing(@Param("followerId") Long followerId);

    @Query(value = "select count(id) from relationship where following_id = :followingId", nativeQuery = true)
    public Integer findCountOfFan(@Param("followingId") Long fanId);

    @Query(value = "select * from relationship where (follower_id = :m1Id and following_id = :m2Id) OR (follower_id = :m2Id and following_id = :m1Id)", nativeQuery = true)
    public List<Relationship> findFollowingRelation(@Param("m1Id") Long m1Id, @Param("m2Id") Long m2Id);

    public Page<Relationship> findByFollowerId(Long followerId, Pageable pageable);

    public Page<Relationship> findByFollowingId(Long followingId, Pageable pageable);

    public List<Relationship> findByFollowerId(Long followingId);


    @Query(value = "SELECT count(*)  from relationship r1, relationship r2  where r1.follower_id=r2.following_id and r1.following_id =r2.follower_id and r1.follower_id=:memberId", nativeQuery = true)
    Integer findCountOfFriend(@Param("memberId") Long memberId);


    @Query(value = "SELECT r2.follower_id from relationship r1, relationship r2  where r1.follower_id=r2.following_id and r1.following_id =r2.follower_id and r1.follower_id=:memberId  limit :page ,:size", nativeQuery = true)
    List<Long> findFriendId(@Param("memberId") Long id, @Param("page") int i, @Param("size") int pageSize);


    @Query(value = "select m.character_id from member m,relationship r where r.following_id =:id and r.follower_id = m.id", nativeQuery = true)
    List<String> findFanCharacterIds(@Param("id") Long id);
}