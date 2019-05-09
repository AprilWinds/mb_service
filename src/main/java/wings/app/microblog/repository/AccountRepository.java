package wings.app.microblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wings.app.microblog.entity.Member;

public interface AccountRepository extends JpaRepository<Member,Long> {







}
