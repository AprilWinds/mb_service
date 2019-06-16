package chat.hala.app.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import javax.persistence.*;
import java.util.Date;


//@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity
@Table(name="dynamic")
public class Dynamic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "img_url")
    private String imgUrl;

    private String content;

    private Integer star;

    @Column(name = "comment_number")
    private Integer commentNumber;

    @Column(name = "read_number")
    private Integer readNumber;

    @Column(columnDefinition = "POINT")
    private Point locate;

    private Date time;

    @Enumerated
    private Type type;

    private String position;


    @Transient
    private String nickname;

    @Transient
    private String avatarUrl;

    @Transient
    private Member.Gender gender;

    @Transient
    private String age;

    @Transient
    private Double distance;

    @Transient
    private Integer isPraise;

    @Transient
    private Member.RelationType relation;

    @Transient
    private String  characterId;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(Integer isPraise) {
        this.isPraise = isPraise;
    }

    public Member.RelationType getRelation() {
        return relation;
    }

    public void setRelation(Member.RelationType relation) {
        this.relation = relation;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public Dynamic() {
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Integer getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(Integer readNumber) {
        this.readNumber = readNumber;
    }


    public Dynamic(Long id, Long memberId, String imgUrl, String content, Integer star, Integer comment, Integer read, Date time, Type type, String position, String avatarUrl, String nickname, Member.Gender gender, String age,String characterId) {
        this.id = id;
        this.memberId = memberId;
        this.imgUrl = imgUrl;
        this.content = content;
        this.star = star;
        this.commentNumber = comment;
        this.readNumber = read;
        this.time = time;
        this.type = type;
        this.position = position;
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.characterId=characterId;
    }

    public  enum Type{
        invisible(0),
        me(1),
        friend(2),
        follow(3),
        open(4);

        private  int state;

        Type(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

    }
}



