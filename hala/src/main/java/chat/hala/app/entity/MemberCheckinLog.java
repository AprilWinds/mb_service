package chat.hala.app.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by astropete on 2018/2/24.
 */
@Entity
@Table(name = "member_checkin_log")
public class MemberCheckinLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Integer continuousCount;
    private Date createdAt;

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

    public Integer getContinuousCount() {
        return continuousCount;
    }

    public void setContinuousCount(Integer continuousCount) {
        this.continuousCount = continuousCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
