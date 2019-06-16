package chat.hala.app.repository;

import chat.hala.app.entity.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by astropete on 2018/1/26.
 */
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long>{
    @Query(value = "SELECT SUM(ABS(amount)), reference_id FROM coin_transaction WHERE from_action = 0 AND created_at > :startDate group by reference_id order by SUM(ABS(amount)) desc limit :pageOffset, :pageLimit", nativeQuery = true)
    public List<Object[]> findGiftReceiverMemberIdsInPage(@Param("startDate") Date start, @Param("pageOffset") int pageOffset, @Param("pageLimit") int pageLimit);

    @Query(value = "SELECT SUM(ABS(amount)), member_id FROM coin_transaction WHERE from_action = 0 AND created_at > :startDate group by member_id order by SUM(ABS(amount)) desc limit :pageOffset, :pageLimit", nativeQuery = true)
    public List<Object[]> findGiftSenderMemberIdsInPage(@Param("startDate") Date start, @Param("pageOffset") int pageOffset, @Param("pageLimit") int pageLimit);

    @Query(value = "SELECT member_id FROM coin_transaction WHERE from_action = 0 AND through = :through AND created_at > :startDate group by member_id order by SUM(ABS(amount)) desc limit :pageOffset, :pageLimit", nativeQuery = true)
    public List<Long> findGiftSenderMemberIdsByRoomIdInPage(@Param("through") String through, @Param("startDate") Date start, @Param("pageOffset") int pageOffset, @Param("pageLimit") int pageLimit);

    @Query(value = "SELECT COALESCE(SUM(ABS(amount)),0) FROM coin_transaction WHERE from_action = 0 and through = :through AND created_at > :startDate", nativeQuery = true)
    public Integer findGiftTotalValueByRoomAndDate(@Param("through") String through, @Param("startDate") Date start);

    @Query(value = "SELECT SUM(ABS(amount)),through FROM coin_transaction WHERE from_action in (0, 1, 2) AND through <> 'CHAT' AND created_at > :startDate group by through order by SUM(ABS(amount)) desc limit :pageOffset, :pageLimit", nativeQuery = true)
    public List<Object[]> findWealthRoomIdsInPage(@Param("startDate") Date start, @Param("pageOffset") int pageOffset, @Param("pageLimit") int pageLimit);

    @Query(value = "SELECT reference_id from coin_transaction where member_id =:memberId AND reference_id in (:referenceIds) and created_at > :startDate and from_action = 4", nativeQuery = true)
    public List<BigInteger> findMemberRewardedDailyTaskIds(@Param("memberId") Long memberId, @Param("referenceIds") List<Long> referenceIds, @Param("startDate") Date startDate);

    @Query(value = "SELECT reference_id from coin_transaction where member_id =:memberId AND reference_id in (:referenceIds) and from_action = 4", nativeQuery = true)
    public List<BigInteger> findMemberRewardedTaskIds(@Param("memberId") Long memberId, @Param("referenceIds") List<Long> referenceIds);

    public CoinTransaction findTop1ByMemberIdAndReferenceIdAndFromActionOrderByCreatedAtDesc(Long memberId, Long referenceId, CoinTransaction.FromAction fromAction);

    @Query(value = "SELECT COALESCE(SUM(ABS(amount)),0) FROM coin_transaction WHERE from_action = 0 and through = :through AND created_at > :startDate and member_id = :memberId", nativeQuery = true)
    public Integer findGiftSenderSpentByRoomIdAndMemberId(@Param("through") String through, @Param("startDate") Date start, @Param("memberId") Long memberId);

    @Query(value = "SELECT COUNT(id) FROM coin_transaction WHERE reference_id in (:refIds) and created_at > current_date and member_id = :memberId and through = 'TASK'", nativeQuery = true)
    public Integer findCountOfDailyRewardedTask(@Param("refIds") List<BigInteger> refIds, @Param("memberId") Long memberId);

    @Query(value = "SELECT COUNT(id) FROM coin_transaction WHERE reference_id in (:refIds) and member_id = :memberId and through = 'TASK'", nativeQuery = true)
    public Integer findCountOfNonDailyRewardedTask(@Param("refIds") List<BigInteger> refIds, @Param("memberId") Long memberId);
}
