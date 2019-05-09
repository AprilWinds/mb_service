package wings.app.microblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.Member;

import java.util.List;

@Repository
public interface MemberRepository  extends JpaRepository<Member,Long> {

    @Query(value = "SELECT * FROM member m WHERE m.username=:username and m.password= :password ", nativeQuery = true)
    Member findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query(value = "select * from member m where m.character_id=:characterId ",nativeQuery =true)
    Member findByCharacterId(@Param("characterId") String characterId);

    @Query(value = "select * from member m where m.username=:username",nativeQuery = true)
    Member findByUsername(@Param("username") String username);


    @Query(value = "select m from Member m")
    List<Member> findMemberByPage(Pageable pageable);

    @Query(value = "select m from Member m where m.nickname like %?1% ")
    List<Member> findByKey(@Param("key") String key);

    @Query(value = "select m.id from Member m where m.isActive=1 ")
    List<Long> findAllId();
}
