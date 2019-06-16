package chat.hala.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by astropete on 2018/1/27.
 */

@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String characterId;
    private Long ownerId;
    private String name;
    private String introduction;
    private String avatarUrl;
   /* @Enumerated
    private RoomStyle style;*/

    @Column(name="style_Id")
    private Long styleId;

    private Integer wealth;
    private Integer attendersCount;
    private Integer fansCount;
    private Integer insidersCount;
    private Integer microphoneLimit;
    @Enumerated
    private MicFacing microphoneFacing;
    private Integer microphonePrice;
    private String microphoneSwitched;
    private Integer insiderPrice;
    @JsonIgnore
    private Integer adminLimit;
    @JsonIgnore
    private Integer attenderLimit;
    @JsonIgnore
    @Column(columnDefinition = "POINT")
    private Point locate;
    private String placeName;
    private Integer hotweight;
    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    @Transient
    private Boolean isFollow;
    // should be owner, edit later
    @Transient
    private Member member;
    @Transient
    private RoomRole.MiR role;

    @Transient
    private String  style;


//    @Transient
//    private RoomStyle roomStyle;


    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }


    public Integer getWealth() {
        return wealth;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

    public Integer getAttendersCount() {
        return attendersCount;
    }

    public void setAttendersCount(Integer attendersCount) {
        this.attendersCount = attendersCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getInsidersCount() {
        return insidersCount;
    }

    public void setInsidersCount(Integer insidersCount) {
        this.insidersCount = insidersCount;
    }

    public Integer getMicrophoneLimit() {
        return microphoneLimit;
    }

    public void setMicrophoneLimit(Integer microphoneLimit) {
        this.microphoneLimit = microphoneLimit;
    }

    public MicFacing getMicrophoneFacing() {
        return microphoneFacing;
    }

    public void setMicrophoneFacing(MicFacing microphoneFacing) {
        this.microphoneFacing = microphoneFacing;
    }

    public Integer getMicrophonePrice() {
        return microphonePrice;
    }

    public void setMicrophonePrice(Integer microphonePrice) {
        this.microphonePrice = microphonePrice;
    }

    public String getMicrophoneSwitched() {
        return microphoneSwitched;
    }

    public void setMicrophoneSwitched(String microphoneSwitched) {
        this.microphoneSwitched = microphoneSwitched;
    }

    public Integer getInsiderPrice() {
        return insiderPrice;
    }

    public void setInsiderPrice(Integer insiderPrice) {
        this.insiderPrice = insiderPrice;
    }

    public Integer getAdminLimit() {
        return adminLimit;
    }

    public void setAdminLimit(Integer adminLimit) {
        this.adminLimit = adminLimit;
    }

    public Integer getAttenderLimit() {
        return attenderLimit;
    }

    public void setAttenderLimit(Integer attenderLimit) {
        this.attenderLimit = attenderLimit;
    }

    public Point getLocate() {
        return locate;
    }

    public void setLocate(Point locate) {
        this.locate = locate;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }

    public Integer getHotweight() {
        return hotweight;
    }

    public void setHotweight(Integer hotweight) {
        this.hotweight = hotweight;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public RoomRole.MiR getRole() {
        return role;
    }

    public void setRole(RoomRole.MiR role) {
        this.role = role;
    }

   /* public enum RoomStyle {
        standard,
        garden,
        island,
        castle
    }
*/
    public enum MicFacing {
        everyone,
        insider
    }

    @JsonIgnore
    public List<Integer> getStandardMicNumbersList(){
        List<Integer> re = new ArrayList<>();
        int i = 1;
        while(i <= this.getMicrophoneLimit()){
            re.add(i);
            i += 1;
        }
        return re;
    }

    @JsonIgnore
    public List<Integer> getSwitchedMicNumbersList(){
        if(Strings.isNullOrEmpty(microphoneSwitched)) return new ArrayList<>();
        return Arrays.stream(Stream.of(this.getMicrophoneSwitched().split(",")).mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList());
    }

}
