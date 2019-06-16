package chat.hala.app.repository;

import chat.hala.app.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface StarRepository extends JpaRepository<Star,Long> {




    @Modifying
    @Query(value = "update star s set s.state=0 ,s.time=:date where s.dynamic_id= :dynamicId and s.from_id= :fromId",nativeQuery = true)
    void updateStarByStateReduce(@Param("fromId") Long fromId, @Param("dynamicId") Long dynamicId, @Param("date") Date date);


    @Modifying
    @Query(value = "update star s set s.state=1 ,s.time=:time where s.id= :id",nativeQuery = true)
    void updateStarByStateIncrease(@Param("id")Long id,@Param("time") Date date);

    @Query(value = "select * from star s where s.dynamic_id=:id and s.from_id= :fromId",nativeQuery = true)
    Star findStarByDynamicId(@Param("id") Long dynamicId ,@Param("fromId")Long from);


    @Query(value = "select count(*) from star s where s.dynamic_id= :dynamicId  and s.from_id= :fromId and s.state=1",nativeQuery = true)
    Integer findMemberStarState(@Param("dynamicId")Long dynamicId ,@Param("fromId")Long fromId);
}
