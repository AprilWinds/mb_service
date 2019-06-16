package chat.hala.app.repository;

import chat.hala.app.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {



    @Query(value = "select t.id from Tag t ")
    List<Long> findIds();
}
