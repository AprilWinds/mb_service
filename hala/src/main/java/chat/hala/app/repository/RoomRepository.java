package chat.hala.app.repository;

import chat.hala.app.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by astropete on 2018/1/27.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {
    public List<Room> findByOwnerId(Long memberId);
    public Page<Room> findAll(Pageable pageable);
    public Page<Room> findByNameLikeAndIsActiveTrue(String like, Pageable pageable);
    public List<Room> findByNameLike(String like);
    public Page<Room> findByIdInAndIsActiveTrueAndOwnerIdIsNot(List<Long> roomIds, Long ownerId, Pageable pageable);

    @Query(value = "select * from room where is_active = 1 and owner_id <> :ownerId and owner_id not in (select id from `member` where is_online = 0) order by created_at desc, attenders_count desc limit :pageOffset, :pageLimit", nativeQuery = true)
    public List<Room> findByLatestAndIsActiveTrue(@Param("pageOffset") int pageOffset, @Param("pageLimit") int pageLimit, @Param("ownerId") Long ownerId);

    @Query(value = "select * from room where is_active = 1 and id in (:idList) and owner_id <> :ownerId order by wealth desc", nativeQuery = true)
    public List<Room> findByIdInOrderByWealthDesc(@Param("idList") List<Long> idList, @Param("ownerId") Long ownerId);

    @Query(value = "select *, (3959 * acos(cos(radians(:lng)) * cos(radians(X(`locate`))) * cos(radians(Y(`locate`)) - radians(:lat)) + sin(radians(:lng)) * sin(radians(X(`locate`))))) as distance from `room` where `locate` is not NULL and owner_id <> :ownerId and owner_id not in (select id from `member` where is_online = 0) and `is_active` = 1 ORDER BY distance asc LIMIT :pageindex, :pagesize", nativeQuery = true)
    public List<Room> findNearby(@Param("lng") double lng, @Param("lat") double lat, @Param("pageindex") Integer pageindex, @Param("pagesize") Integer pagesize, @Param("ownerId") Long ownerId);

    @Query(value = "select * from room where is_active = 1 order by attenders_count desc limit 1", nativeQuery = true)
    public Room findMostAttendersRoom();

    @Query(value = "select * from room where is_active = 1 order by wealth desc limit 1", nativeQuery = true)
    public Room findMostWealthRoom();

    @Query(value = "select * from room where is_active = 1 and owner_id not in (select id from `member` where is_online = 0) order by (attenders_count / :amc * 0.6 + wealth / :wmc * 0.4) DESC limit :pageindex, :pagesize", nativeQuery = true)
    public List<Room> findExplore(@Param("amc") Integer amc, @Param("wmc") Integer wmc, @Param("pageindex") Integer pageIndex, @Param("pagesize") Integer pagesize);

    @Query(value = "select * from room where place_name like :place and is_active = 1 order by (attenders_count / :amc * 0.6 + wealth / :wmc * 0.4) DESC limit :pageindex, :pagesize", nativeQuery = true)
    public List<Room> findExploreInCountry(@Param("place") String place, @Param("amc") Integer amc, @Param("wmc") Integer wmc, @Param("pageindex") Integer pageIndex, @Param("pagesize") Integer pagesize);

    @Query(value = "SELECT * from `room` where is_active = 1 and id in (:idList) order by field(id, :idList)", nativeQuery = true)
    public List<Room> findByIdListOrderByIdList(@Param("idList") List idList);

    public int countByCreatedAtAfterAndCreatedAtBefore(Date start, Date end);

}
