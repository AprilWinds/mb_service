package chat.hala.app.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by astropete on 2018/3/19.
 */
@Entity
@Table(name = "room_banning")
public class RoomBanning {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private Long memberId;
    private Long banMemberId;
    @Enumerated
    private RoomBan acting;
    private Long referenceId;
    private Date createdAt;
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBanMemberId() {
        return banMemberId;
    }

    public void setBanMemberId(Long banMemberId) {
        this.banMemberId = banMemberId;
    }

    public RoomBan getActing() {
        return acting;
    }

    public void setActing(RoomBan acting) {
        this.acting = acting;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum RoomBan{
        out,
        offmic,
        silence
    }
}
