package chat.hala.app.entity;


import javax.persistence.*;

@Entity
@Table(name = "room_background")
public class RoomBackground {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="bg_type")
    private Integer backgroundType;

    @Column(name = "avatar_url")
    private String avatarUrl;


}