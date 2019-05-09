package wings.app.microblog.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="favorite")
public class Favorite {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long collectorId;

    private Long momentId;

}
