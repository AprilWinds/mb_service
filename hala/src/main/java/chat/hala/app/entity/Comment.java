package chat.hala.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dynamic_id")
    private Long dynamicId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "from_id")
    private Long fromId;

    @Column(name = "to_id")
    private Long toId;

    private String content;

    private Date time;

    private String position;

    private Point locate;

    @Transient
    private String distance;

    @Transient
    private String fromName;

    @Transient
    private String fromAvatar;

    @Transient
    private String fromGender;

    @Transient
    private String fromCharctetrId;

    @Transient
    private String fromAge;

    @Transient
    private String toName;

    @Transient
    private String toAvatar;

    @Transient
    private String toCharacterId;

    @Transient
    private List<Comment> comments;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Long getDynamicId() {
        return dynamicId;
    }

    @JsonProperty
    public void setDynamicId(Long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Point getLocate() {
        return locate;
    }

    @JsonIgnore
    public void setLocate(Point locate) {
        this.locate = locate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }


    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }



    public String getFromGender() {
        return fromGender;
    }

    public void setFromGender(String fromGender) {
        this.fromGender = fromGender;
    }


    public String getFromAge() {
        return fromAge;
    }

    public void setFromAge(String fromAge) {
        this.fromAge = fromAge;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }

    public String getFromCharctetrId() {
        return fromCharctetrId;
    }

    public void setFromCharctetrId(String fromCharctetrId) {
        this.fromCharctetrId = fromCharctetrId;
    }

    public String getToCharacterId() {
        return toCharacterId;
    }

    public void setToCharacterId(String toCharacterId) {
        this.toCharacterId = toCharacterId;
    }


}




