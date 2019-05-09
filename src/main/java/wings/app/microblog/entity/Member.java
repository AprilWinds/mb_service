package wings.app.microblog.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "character_id")
    private String characterId;

    private String username;

    private String password;

    private String nickname;

    private String introduction;

    private String address;

    private String avatarUrl;

    private String dob;

    private String age;

    private String gender;

    private Integer isActive;

    private String rongToken;

    private Date createdAt;

    private Date updatedAt;

    private String company;

    @Transient
    private String appToken;


    @Transient
    private Integer fansCount;

    @Transient
    private Integer followingCount;

    @Transient
    private Integer momentsCount;

    @Transient
    private Integer  relation;


    @Transient
    private List<Moment>  moments;

}





