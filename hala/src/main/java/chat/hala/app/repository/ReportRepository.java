package chat.hala.app.repository;

import chat.hala.app.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/1/25.
 */
public interface ReportRepository extends JpaRepository<Report, Long> {
}
