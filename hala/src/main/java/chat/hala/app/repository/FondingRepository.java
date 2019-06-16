package chat.hala.app.repository;

import chat.hala.app.entity.Fonding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/1/7.
 */
public interface FondingRepository extends JpaRepository<Fonding, Long> {
    public Fonding findById(Long id);
    public Page<Fonding> findByParentId(Long parentId, Pageable pageable);
}
