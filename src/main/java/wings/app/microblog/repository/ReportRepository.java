package wings.app.microblog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wings.app.microblog.entity.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query(value = "select r from Report r ")
    List<Report> findByPage(Pageable pageable);
}
