package wings.app.microblog.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="relation_ship")
public class RelationShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long followerId;

    private Long followingId;

    private Date createAt;

    private Date updateAt;
}


