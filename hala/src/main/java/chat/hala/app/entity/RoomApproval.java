package chat.hala.app.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RoomApproval {

    @Id
    private  Long id;
    private  String name;
    private  String avatarUrl;
    private  String introduction;
    private  Integer auditCount;
    private  String  description;


    public RoomApproval (){}

    public RoomApproval(Long id,String  name ,String avatarUrl ,String introduction,Integer auditCount ,String description){
        this.id=id;
        this.name=name;
        this.avatarUrl=avatarUrl;
        this.introduction=introduction;
        this.auditCount=auditCount;
        this.description=description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getAuditCount() {
        return auditCount;
    }

    public void setAuditCount(Integer auditCount) {
        this.auditCount = auditCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
