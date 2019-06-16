package chat.hala.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * Created by astropete on 2017/12/25.
 */
//@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "character_id", unique = true)
    private String characterId;
    private String nickname;
    private String introduction;
    private String hometown;
    private String avatarUrl;

    private String dob;
    @Enumerated
    private Gender gender;
    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;
    private String thirdId;
    private String password;
    private Integer coins;
    private Integer spent;
    private Integer wealth;
    private String rongToken;
    private String apToken;
    @JsonIgnore
    @Column(columnDefinition = "POINT")
    private Point locate;
    private Date lastActive;
    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "member_fonding", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "fonding_id", referencedColumnName = "id"))
    private Set<Fonding> fondings;
    private Date createdAt;
    private Date updatedAt;
    @Column(name = "is_online", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isOnline;
    @Transient
    private Integer followingCount;
    @Transient
    private Integer fanCount;
    @Transient
    @Enumerated(EnumType.STRING)
    private RelationType relation;
    @Transient
    private Integer distance;
    @Transient
    private Boolean hasReward;

    @Column(name = "tag_id")
    private String  tagId;

    @Column(name="current_residence")
    private String  currentResidence;

    private String  source;

    private String  background;

    @Column(name = "salvage_count")
    private Integer salvageCount;

    @Column(name = "bottle_avatar") // 瓶子聊天头像
    private String  bottleAvatar;

    @Column(name = "send_count")//瓶子每日发送次数
    private Integer sendCount;

    @Enumerated
    @Column(name="affective_state")
    private AffectiveState  affectiveState;

    @Transient
    private Set<String>  tags=new HashSet<>();

    @Transient
    private  Integer  friendsCount;

    @Transient
    private  String age;

    @Transient
    private  Date blockingTime;

    @Transient
    private  Integer dynamicCount;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRongToken() {
        return rongToken;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }

    public String getApToken() {
        return apToken;
    }

    public void setApToken(String apToken) {
        this.apToken = apToken;
    }

    public Set<Fonding> getFondings() {
        return fondings;
    }

    public void setFondings(Set<Fonding> fondings) {
        this.fondings = fondings;
    }

    public Point getLocate() {
        return locate;
    }

    public void setLocate(Point locate) {
        this.locate = locate;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getSpent() {
        return spent;
    }

    public void setSpent(Integer spent) {
        this.spent = spent;
    }

    public Integer getWealth() {
        return wealth;
    }

    public void setWealth(Integer wealth) {
        this.wealth = wealth;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public RelationType getRelation() {
        return relation;
    }

    public void setRelation(RelationType relation) {
        this.relation = relation;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Boolean getHasReward() {
        return hasReward;
    }

    public void setHasReward(Boolean hasReward) {
        this.hasReward = hasReward;
    }

    public enum Gender {
        male,
        female,
        unknown
    }

    public enum RelationType {
        none,
        following,
        followed,
        friend,
        blocking,
        blocked,
        hater
    }

    public enum AffectiveState{
        Single,
        In_Love,
        Married,
        Secrecy,
        Same_sex,
    }


    public void discardSensitive(){
        this.setThirdId(null);
        this.setMobileNumber(null);
        this.setRongToken(null);
        this.setPassword(null);
        this.setCoins(null);
    }
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", characterId='" + characterId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", introduction='" + introduction + '\'' +
                ", hometown='" + hometown + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", dob='" + dob + '\'' +
                ", gender=" + gender +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", thirdId='" + thirdId + '\'' +
                ", password='" + password + '\'' +
                ", coins=" + coins +
                ", spent=" + spent +
                ", wealth=" + wealth +
                ", rongToken='" + rongToken + '\'' +
                ", apToken='" + apToken + '\'' +
                ", locate=" + locate +
                ", lastActive=" + lastActive +
                ", isActive=" + isActive +
                ", fondings=" + fondings +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isOnline=" + isOnline +
                ", followingCount=" + followingCount +
                ", fanCount=" + fanCount +
                ", relation=" + relation +
                ", distance=" + distance +
                ", hasReward=" + hasReward +
                '}';
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCurrentResidence() {
        return currentResidence;
    }

    public void setCurrentResidence(String currentResidence) {
        this.currentResidence = currentResidence;
    }





    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public AffectiveState getAffectiveState() {
        return affectiveState;
    }

    public void setAffectiveState(AffectiveState affectiveState) {
        this.affectiveState = affectiveState;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }


    public Date getBlockingTime() {
        return blockingTime;
    }

    public void setBlockingTime(Date blockingTime) {
        this.blockingTime = blockingTime;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getDynamicCount() {
        return dynamicCount;
    }

    public void setDynamicCount(Integer dynamicCount) {
        this.dynamicCount = dynamicCount;
    }

    public Integer getSalvageCount() {
        return salvageCount;
    }

    public void setSalvageCount(Integer salvageCount) {
        this.salvageCount = salvageCount;
    }

    public String getBottleAvatar() {
        return bottleAvatar;
    }

    public void setBottleAvatar(String bottleAvatar) {
        this.bottleAvatar = bottleAvatar;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }
}




