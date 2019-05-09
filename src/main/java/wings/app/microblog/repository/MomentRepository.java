package wings.app.microblog.repository;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;
        import org.springframework.stereotype.Repository;
        import wings.app.microblog.entity.Moment;
        import java.util.List;

@Repository
public interface MomentRepository extends JpaRepository<Moment,Long> {

    @Query(value = "select count(*) from moment m where m.member_id=:id and m.visibility=3",nativeQuery = true)
    Integer getMomentsNum(@Param("id") Long id);


    @Query(value = "select mt.* from moment mt ,member mr where mr.is_active=1 and mt.member_id=mr.id and mt.visibility=3  and mt.member_id !=:id " +
            "and mt.member_id not in (select b.blocking_id from black_list b where b.blocker_id=:id ) order by mt.time desc limit :offset,:pageSize"
            ,nativeQuery = true)
    List<Moment> getAllVisibleMoments(@Param("id") Long id, @Param("offset") long offset, @Param("pageSize") int pageSize);


    @Query(value = "select m from Moment m where m.memberId=:id and m.visibility>0 order by m.time desc")
    List<Moment> findByMemberIdAndVisibility(@Param("id") Long id);

    @Query(value ="select * from moment m where m.member_id=:id and m.visibility>=:visibility",nativeQuery = true)
    List<Moment> findByRelation(@Param("id") Long momentId, @Param("visibility") Integer visibility);
}
