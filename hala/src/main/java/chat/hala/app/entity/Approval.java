package chat.hala.app.entity;

import javax.persistence.*;

@Entity
@Table(name="approval")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "room_id")
    private Long roomId;


    // 待审批0  未通过1  已通过2
    private Integer state;

    @Column(name = "audit_count")
    private Integer auditCount;

    private String description;

    @Transient
    private Member member;

    @Transient
    private Room room;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getAuditCount() {
        return auditCount;
    }

    public void setAuditCount(Integer auditCount) {
        this.auditCount = auditCount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public  Approval(){}

    public Approval(Long id, Long roomId ,Integer state, Integer auditCount ,String description,Long memberId ){
        this.id=id;
        this.state=state;
        this.auditCount=auditCount;
        this.description=description;
        this.roomId=roomId;
    }

    public Approval(Long id, Long memberId ,Integer state, Integer auditCount ,String description){
        this.id=id;
        this.state=state;
        this.auditCount=auditCount;
        this.description=description;
        this.memberId=memberId;
    }


    @Override
    public String toString() {
        return "Approval{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", roomId=" + roomId +
                ", state=" + state +
                ", auditCount=" + auditCount +
                ", description='" + description + '\'' +
                ", member=" + member +
                ", room=" + room +
                '}';
    }
}

