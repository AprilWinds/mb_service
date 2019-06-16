package chat.hala.app.repository;

import chat.hala.app.entity.MemberFonding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Created by astropete on 2018/1/7.
 */
public interface MemberFondingRepository extends JpaRepository<MemberFonding, Long> {

    @Modifying
    @Query(value = "DELETE FROM member_fonding where member_id = :memberId", nativeQuery = true)
    public int deleteAllFondingByMemberId(@Param("memberId") Long memberId);
}
