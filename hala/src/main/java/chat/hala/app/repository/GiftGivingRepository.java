package chat.hala.app.repository;

import chat.hala.app.entity.GiftGiving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by astropete on 2018/1/26.
 */
public interface GiftGivingRepository extends JpaRepository<GiftGiving, Long> {

    @Query(value = "select count(id) from gift_giving where sender_id = :memberId and room_id is not null", nativeQuery = true)
    public int findMemberGiftCountInRoom(@Param("memberId") Long memberId);
}
