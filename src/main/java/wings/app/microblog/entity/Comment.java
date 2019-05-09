package wings.app.microblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long momentId;

    private Long parentId;

    private Long fromId;

    private Long toId;

    private String content;

    private Date time;

    @Transient
    private String avatarUrl;

    @Transient
    private List<Comment> replyList;

    @Transient
    private String fromName;

    @Transient
    private String toName;
}
