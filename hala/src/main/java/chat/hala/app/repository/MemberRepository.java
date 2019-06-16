package chat.hala.app.repository;

import chat.hala.app.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by astropete on 2017/12/25.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByMobileNumberAndIsActiveTrue(String mobileNumber);
    //public Member findByFacebookIdAndIsActiveTrue(String facebookId);
    public Member findByCharacterIdAndIsActiveTrue(String characterId);
    public Member findByCharacterId(String characterId);
    public Page<Member> findByCharacterIdLikeOrNicknameLike(String keyword, String keyword2, Pageable pageable);
    public List<Member> findByCharacterIdLikeOrNicknameLike(String keyword, String keyword2);
    public Member findById(Long id);
    public List<Member> findTop5ByIsActiveTrueOrderByWealthDescCreatedAtAsc();

    @Query(value = "SELECT * FROM `member` WHERE (mobile_number like :semiref) OR (character_id = :reference) LIMIT 1", nativeQuery = true)
    public Member findByMemberReference(@Param("reference") String reference, @Param("semiref") String semiref);

    @Query(value = "SELECT * FROM `member` WHERE (mobile_number like :semiref AND password = :password) OR (character_id = :reference AND password = :password) LIMIT 1", nativeQuery = true)
    public Member findMemberSignIn(@Param("reference") String reference, @Param("semiref") String semiref, @Param("password") String password);

    public List<Member> findByIdInOrderByWealthDesc(List<Long> memberIds);
    public Page<Member> findByIdIn(List<Long> memberIds, Pageable pageable);

    @Query(value = "SELECT * from `member` where id in (:idList) order by field(id, :idList) limit :pageOffset, :pageSize", nativeQuery = true)
    public List<Member> findByIdListOrderByIdListInPage(@Param("idList") List idList, @Param("pageOffset") int pageOffset, @Param("pageSize") int pageSize);

    @Query(value = "SELECT * from `member` where id in (:idList) order by field(id, :idList)", nativeQuery = true)
    public List<Member> findAllByIdListOrderByIdList(@Param("idList") List idList);

    public int countByCreatedAtAfterAndCreatedAtBefore(Date start, Date end);

    @Query(value = "select m.nickname,m.avatar_url,m.gender,m.last_active ,TIMESTAMPDIFF(YEAR, m.dob, CURDATE()) as age, (6371 * acos(cos(radians(:lng)) * cos(radians(X(m.locate))) * cos(radians(Y(m.locate)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(m.locate))))) as distance ,m.introduction ,m.id ,m.character_id from member m where m.id !=:id  and m.id>3 and m.gender like :flag  order by distance limit :page,:size",nativeQuery = true)
    List<Object[]>findNearbyPeople(@Param("id") Long id, @Param("lng") Double lng, @Param("lat") Double lat, @Param("page") Integer page, @Param("size") Integer size,@Param("flag") String flag);

    @Modifying
    @Query(value ="update member m set m.last_active= :time,m.is_online=:i where m.character_id=:userid",nativeQuery = true)
    void cloudUpdateStatus(@Param("userid") String userid, @Param("i") int i, @Param("time") Date date);

    @Modifying
    @Query(value = "update member m set m.locate= GeomFromText(:locate) where m.id =:memberId",nativeQuery = true)
    void triggerUpdateLocate(@Param("locate") String locate, @Param("memberId") Long memberId);


    @Query(value = "select m.nickname,m.avatar_url,m.gender,m.last_active,TIMESTAMPDIFF(YEAR, m.dob, CURDATE()) as age,(6371 * acos(cos(radians(:lng)) * cos(radians(X(m.locate))) * cos(radians(Y(m.locate)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(m.locate))))) as distance ,m.introduction ,m.id, m.character_id  from member m where m.id !=:id and m.id>3 and m.is_online=1 order by distance limit :page,:size",nativeQuery = true)
    List<Object[]> findOnlinePeople(@Param("id") Long id,@Param("lng") Double lng,@Param("lat") Double lat, @Param("page") Integer page, @Param("size") Integer size);


    @Query(value = "SELECT avatar_url FROM member WHERE id >= (SELECT floor(RAND() * (SELECT MAX(id) FROM member)))AND id != :id AND id >3 ORDER BY id LIMIT 0, 9",nativeQuery = true)
    List<String> findOnlineAvatar(@Param("id") Long id);

    @Query(value = "select * from member m where m.third_id=:thirdId and m.source=:from",nativeQuery = true)
    Member findByFromAndThirdId(@Param("thirdId") String thirdId, @Param("from") String from);

    @Query(value = "select m.id from Member m where m.apToken='123'")
    List<Long> findFackMemberIds();
}
