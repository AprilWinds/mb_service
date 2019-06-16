package chat.hala.app.repository;

import chat.hala.app.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by astropete on 2018/6/16.
 */
public interface RechargeRepository extends JpaRepository<Recharge, Long> {

    @Query(value = "select COALESCE(SUM(amount),0) from recharge where state = 1", nativeQuery = true)
    public Double sumRecharge();

    public Recharge findByOutTransactionCode(String outTransactionCode);
}
