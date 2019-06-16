package chat.hala.app.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "drift_bottle")
public class DriftBottle {

    @Id
    @Column(name = "id")
    private  Long id;

    @Column(name = "sender_id")
    private  Long senderId;

    private  String content;

    @Column(name = "salvage_id")
    private  Long  salvageId;

    private Date time;

    @Transient
    private String avatarUrl;

    @Transient
    private Member.Gender gender;

    @Transient
    private String characterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Member.Gender getGender() {
        return gender;
    }

    public void setGender(Member.Gender gender) {
        this.gender = gender;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public Long getSalvageId() {
        return salvageId;
    }

    public void setSalvageId(Long salvageId) {
        this.salvageId = salvageId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

