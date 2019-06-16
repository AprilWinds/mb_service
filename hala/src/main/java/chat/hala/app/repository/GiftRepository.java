package chat.hala.app.repository;

import chat.hala.app.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by astropete on 2018/1/26.
 */
public interface GiftRepository extends JpaRepository<Gift, Long> {
    public Gift findById(Long id);

    @Query(value = "select * from gift order by price asc", nativeQuery = true)
    public List<Gift> findAllOrderByPriceAsc();
}
