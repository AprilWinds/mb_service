package wings.app.microblog.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="web_admin")
public class WebAdmin {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Transient
    private String appToken;

}
