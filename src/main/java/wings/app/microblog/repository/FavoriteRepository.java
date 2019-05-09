package wings.app.microblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.Favorite;

import java.util.List;


@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {


    @Query("select f from Favorite f where f.collectorId=:cid and f.momentId=:mid")
    Favorite findByCollectorId(@Param("cid") Long cid, @Param("mid") Long mid);

    @Query("select f from Favorite f where f.collectorId=:id")
    List<Favorite> findMyFavorite(@Param("id") Long id);
}
