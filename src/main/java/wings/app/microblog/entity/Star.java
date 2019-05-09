package wings.app.microblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="star")
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private  Long  id;

    private  Long momentId;

    private  Long fromId;

    private  Long toId;

    private  Date time;

    private  Integer isDelete;
}
