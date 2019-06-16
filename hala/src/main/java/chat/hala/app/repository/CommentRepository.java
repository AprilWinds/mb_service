package chat.hala.app.repository;

import chat.hala.app.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment,Long> {


   @Query(value = "SELECT  c.*,(6371 * acos(cos(radians(:lng)) * cos(radians(X(c.locate))) * cos(radians(Y(c.locate)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(c.locate))))) as distance from `comment` c WHERE c.dynamic_id=:dynamicId and c.parent_id= 0  ORDER BY c.time limit :page,:size",nativeQuery = true)
    List<Comment> findAllByDynamicId(@Param("dynamicId") Long dynamicId, @Param("page") Integer page, @Param("size") Integer size,@Param("lat")Double lat,@Param("lng")Double lng);



    @Query(value = "select c.* ,(6371 * acos(cos(radians(:lng)) * cos(radians(X(c.locate))) * cos(radians(Y(c.locate)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(c.locate))))) as distance from `comment` c where c.parent_id=:parentId order by time ",nativeQuery = true)
    List<Comment> findByParentId(@Param("parentId")Long parentId,@Param("lat")Double lat,@Param("lng")Double lng);


    @Modifying
    @Query(value = "insert into comment(dynamic_id,parent_id,from_id,to_id,content,locate,position,time) values(:dynamicId,:parentId,:fromId,:toId,:content,GeomFromText(:locate),:position,:time) ",nativeQuery = true)
    void insert(@Param("dynamicId") Long dynamicId, @Param("parentId") Long parentId,@Param("fromId") Long fromId, @Param("toId") Long toId, @Param("content")String content,@Param("locate") String locate,@Param("position")String position,@Param("time") Date date);


}

