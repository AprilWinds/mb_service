package chat.hala.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Created by astropete on 2018/2/24.
 */
@Entity
@Table(name = "member_setting")
public class MemberSetting {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    @JsonIgnore
    private Integer push;
    @Column(name = "distance", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean distance;
    @Column(name = "live", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean live;
    private Date createdAt;
    private Date updatedAt;
    @Transient
    private Map<String, Boolean> pushItems;


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

    public Integer getPush() {
        return push;
    }

    public void setPush(Integer push){
        this.push = push;
    }

    public Boolean getDistance() {
        return distance;
    }

    public void setDistance(Boolean distance) {
        this.distance = distance;
    }

    public Boolean getLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
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

    public Map<String, Boolean> getPushItems(){
        if(this.push == null){
            return this.pushItems;
        }
        List<String> subitems = Arrays.asList(String.format("%03d", Integer.valueOf(Integer.toBinaryString(this.push))).split("(?!^)"));
        Map<String, Boolean> pushItems = new HashMap<>();
        pushItems.put("greeting", "1".equals(subitems.get(0)));
        pushItems.put("room", "1".equals(subitems.get(1)));
        pushItems.put("online", "1".equals(subitems.get(2)));
        return pushItems;
    }

    public void setPushItems(Map<String, Boolean> pushItems) {
        this.pushItems = pushItems;
    }
}
