package wings.app.microblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wings.app.microblog.entity.WebAdmin;

@Repository
public interface WebAdminRepository extends JpaRepository<WebAdmin,Long> {


     @Query(value = "select w from WebAdmin w where w.username=:username and w.password=:password")
     WebAdmin findByContent(@Param("username") String username, @Param("password") String password);
}
