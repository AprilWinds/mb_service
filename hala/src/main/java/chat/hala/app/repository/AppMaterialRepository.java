package chat.hala.app.repository;

import chat.hala.app.entity.AppMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by astropete on 2018/4/9.
 */
public interface AppMaterialRepository extends JpaRepository<AppMaterial, Long> {
    @Query(value = "select * from app_material where category = :category order by sortby desc", nativeQuery = true)
    public List<AppMaterial> findByCategoryOrderBySortBy(@Param("category") Integer category);
}
