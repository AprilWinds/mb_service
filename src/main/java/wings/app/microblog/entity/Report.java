package wings.app.microblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    private  Long  reportMemberId;

    private  Long  momentId;

    private  Long  reporterId;

    private  String reason;

    private  String description;

    private  Date  time;


    @Transient
    private  String  content;

    @Transient
    private  String  img;

    @Transient
    private  String  reportMemberName;

    @Transient
    private  String  reporterName;

}
