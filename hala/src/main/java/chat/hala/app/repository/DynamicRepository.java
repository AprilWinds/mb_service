package chat.hala.app.repository;

        import chat.hala.app.entity.Dynamic;
        import chat.hala.app.restful.wrapper.DynamicWrapper;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;
        import org.springframework.transaction.annotation.Transactional;

        import javax.persistence.metamodel.StaticMetamodel;
        import java.util.Date;
        import java.util.List;

public interface DynamicRepository extends JpaRepository<Dynamic,Long> {




    @Query(value = "select new chat.hala.app.entity.Dynamic(d.id,d.memberId,d.imgUrl,d.content,d.star,d.commentNumber,d.readNumber,d.time,d.type,d.position,m.avatarUrl,m.nickname,m.gender,m.dob,m.characterId) from Dynamic d, Member m where d.memberId in "+
    "(select r.followingId from Relationship r where r.followerId =:id) and d.memberId=m.id  and d.type>2  order by d.time desc")
    List<Dynamic> findAllByFollow(@Param("id") Long id, Pageable pageable);



    @Query(value = "select count(*) from dynamic where member_id in" +
            "(select following_id from relationship  r where follower_id =:id)",nativeQuery = true)
    Long findDynamicCount(@Param("id")Long id);
/*

    @Query(value = "select  d.*,m.nickname,m.avatar_url,m.gender,TIMESTAMPDIFF(YEAR, m.dob, CURDATE()) as age,(6371 * acos(cos(radians(:lng)) * cos(radians(X(d.locate))) * cos(radians(Y(d.locate)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(d.locate))))) AS distance from dynamic d,member m where d.member_id <> :id and d.member_id=m.id and d.type=1 HAVING distance <10 order by distance limit :page,:size",nativeQuery = true)
    List<Dynamic> findNearbyDynamic(@Param("id") Long id, @Param("lng") Double lng, @Param("lat") Double lat, @Param("page") Integer page, @Param("size") Integer size);

*/
    @Query(value = "select d.*,(6371 * acos(cos(radians(:lng)) * cos(radians(X(d.locate))) * cos(radians(Y(d.locate)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(d.locate))))) AS distance from dynamic d,member m where d.member_id != :id  and d.member_id not in(select  b.blocking_member_id from  blocking b where b.blocker_id= :id  ) and   d.member_id=m.id  and d.type>3 HAVING distance <10 order by d.time desc limit :page,:size",nativeQuery = true)
    List<Dynamic> findNearbyDynamic(@Param("id") Long id, @Param("lng") Double lng, @Param("lat") Double lat, @Param("page") Integer page, @Param("size") Integer size);


    @Query(value = "select count(*) from dynamic d where d.member_id=:id and d.type>0 ",nativeQuery = true)
    Integer findDynamicCountById(@Param("id")Long id);

    /*@Query(value = "select new chat.hala.app.restful.wrapper.DynamicWrapper(d.id,d.memberId,d.imgUrl,d.content,d.star,d.comment,d.read,d.time,d.type,d.position,m.avatarUrl,m.nickname,m.gender,m.dob,d.locate)  from Dynamic d , Member m where"
    +" d.memberId= m.id and d.memberId <>:id  order by d.time ")
    List<DynamicWrapper> findNearbyDynamic(@Param("id") Long id, Pageable pageable);*/


    @Modifying
    @Query(value = "update dynamic d set d.star=d.star+1 where d.id= :dynamicId",nativeQuery = true)
    void updateDynamicByStarIncrease(@Param("dynamicId") Long dynamicId);


    @Modifying
    @Query(value = "update dynamic d set d.star=d.star-1 where d.id= :dynamicId",nativeQuery = true)
    void updateDynamicByStarReduce(@Param("dynamicId") Long dynamicId);

    @Modifying
    @Query(value = "update dynamic d set d.comment=d.comment+1 where d.id= :dynamicId",nativeQuery= true)
    void updateDynamicByCommentsIncrease(@Param("dynamicId") Long dynamicId);


    @Transactional
    @Modifying
    @Query(value = "insert  into dynamic (member_id,img_url,content,locate,position,type,time)  values(:memberId,:imgUrl,:content,GeomFromText(:locate),:position,:type,:time)",nativeQuery = true)
    void insert(@Param("memberId") Long id, @Param("imgUrl") String imgUrl, @Param("content") String content, @Param("locate") String locate,@Param("position") String position, @Param("type") Integer type, @Param("time") Date date);


    @Query(value = "select d.* from dynamic d where d.member_id=:id and d.type > :type  order by d.time DESC limit :page, :size ",nativeQuery = true)
    List<Dynamic> findByMemberIdAndTypePage(@Param("id") Long id, @Param("type") int type, @Param("page") Integer page, @Param("size") Integer size);


    @Transactional
    @Modifying
    @Query(value = "update dynamic d set d.read = d.read+1 where d.id=:id",nativeQuery = true)
    void updateDynamicByReadIncrease(@Param("id") Long dynamicId);

    @Query(value = "select LAST_INSERT_ID()",nativeQuery = true)
    Long findLastInstanceId();

    @Modifying
    @Query(value = "update dynamic d set d.type =:type where d.id = :id",nativeQuery = true)
    void updateDynamicByPrivilege( @Param("type") Integer type, @Param("id") Long dynamicId);


    @Modifying
    @Query(value = "update dynamic d set d.is_hide=0 where d.id= :id",nativeQuery = true)
    void hideDynamic(@Param("id") Long dynamicId);
}
