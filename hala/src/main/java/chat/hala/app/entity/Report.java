package chat.hala.app.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by astropete on 2018/1/25.
 */

@Entity
@Table(name = "report")
public class Report {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reporterId;
    private Long reportMemberId;
    private String reason;
    private String description;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;
    @Transient
    private Member reporter;
    @Transient
    private Member reportMember;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getReportMemberId() {
        return reportMemberId;
    }

    public void setReportMemberId(Long reportMemberId) {
        this.reportMemberId = reportMemberId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Member getReporter() {
        return reporter;
    }

    public void setReporter(Member reporter) {
        this.reporter = reporter;
    }

    public Member getReportMember() {
        return reportMember;
    }

    public void setReportMember(Member reportMember) {
        this.reportMember = reportMember;
    }
}
