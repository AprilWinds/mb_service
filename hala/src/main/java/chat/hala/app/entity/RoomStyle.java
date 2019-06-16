package chat.hala.app.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="room_style")
public class RoomStyle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  Integer level;
    private  String  name;
    private  Integer coin;
    private  Integer admin;
    private  Integer attender;

    @Override
    public String toString() {
        return "RoomStyle{" +
                "id=" + id +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", coin=" + coin +
                ", admin=" + admin +
                ", attender=" + attender +
                ", bgId='" + bgId + '\'' +
                '}';
    }

    @Column(name="bg_id")
    private  String  bgId;


    @Transient
    private Set<RoomBackground> rbSet=new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getAttender() {
        return attender;
    }

    public void setAttender(Integer attender) {
        this.attender = attender;
    }

    public String getBgId() {
        return bgId;
    }

    public void setBgId(String bgId) {
        this.bgId = bgId;
    }

    public Set<RoomBackground> getRbSet() {
        return rbSet;
    }

    public void setRbSet(Set<RoomBackground> rbSet) {
        this.rbSet = rbSet;
    }
}
