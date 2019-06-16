package chat.hala.app.repository;

import chat.hala.app.entity.WebAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/6/14.
 */
public interface WebAdminRepository extends JpaRepository<WebAdmin, Long> {

    public WebAdmin findByNameAndPassword(String name, String password);
}
