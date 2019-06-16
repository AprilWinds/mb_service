package wings.app.microblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name="moment")
public class Moment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long  memberId;

    private String imgUrl;

    private String content;

    private Integer starNum;

    private Integer commentNum;

    private Date time;

    private Integer visibility;

    @Transient
    private String nickname;

    @Transient
    private String avatarUrl;

    @Transient
    private Integer isPraise;

    @Transient
    private Integer isMark;

    @Transient
    private Integer relation;

    public enum Visibility{
        none(0),
        myself(1),
        followed(2),
        open(3);

        private Integer id;

        public Integer getId() {
            return id;
        }

        Visibility(Integer id) {
            this.id = id;
        }
    }
}
