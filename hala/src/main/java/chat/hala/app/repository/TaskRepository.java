package chat.hala.app.repository;

import chat.hala.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/3/7.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    public Task findBySymbol(String symbol);
}
