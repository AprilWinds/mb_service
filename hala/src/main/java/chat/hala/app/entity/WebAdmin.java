package chat.hala.app.entity;

import javax.persistence.*;

/**
 * Created by astropete on 2018/6/14.
 */
@Entity
@Table(name = "web_admin")
public class WebAdmin {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @Enumerated
    private WARole role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WARole getRole() {
        return role;
    }

    public void setRole(WARole role) {
        this.role = role;
    }

    public enum WARole {
        superadmin,
        customerservice
    }
}
