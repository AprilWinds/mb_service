package wings.app.microblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "msg")
public class Msg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long fromId;
    private Long toId;
    private String fromAvatar;
    private String fromNickname;
    private String msg;
    //0 notification 1 message
    private Integer type;

    private Date  time;

}
