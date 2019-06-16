package chat.hala.app.repository;

import chat.hala.app.entity.DynamicReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DynamicReportRepository extends JpaRepository<DynamicReport,Long> {


    @Query(value = "select * from dynamic_report d where d.handler=0  limit :page,:size",nativeQuery = true)
    List<DynamicReport> findAllByHandler(@Param("page") int page, @Param("size") int size);

    @Modifying
    @Query(value = "update dynamic d set d.type=0 where d.id= :id",nativeQuery = true)
    void updateISHide(@Param("id") Long id);
}
