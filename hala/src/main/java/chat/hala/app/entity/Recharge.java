package chat.hala.app.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by astropete on 2018/6/16.
 */

@Entity
@Table(name = "recharge")
public class Recharge {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    @Enumerated
    private PayThrough through;
    private String outTransactionCode;
    private String transactionId;
    private Double amount;
    @Enumerated
    private OState state;
    private Date createdAt;
    private Date updatedAt;
    @Transient
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public PayThrough getThrough() {
        return through;
    }

    public void setThrough(PayThrough through) {
        this.through = through;
    }

    public String getOutTransactionCode() {
        return outTransactionCode;
    }

    public void setOutTransactionCode(String outTransactionCode) {
        this.outTransactionCode = outTransactionCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public OState getState() {
        return state;
    }

    public void setState(OState state) {
        this.state = state;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public enum PayThrough {
        apppay,
        creditcard,
        ebay,
        alipay,
        wechat,
        admin,
        unknown
    }

    public enum OState {
        init,
        success,
        failed,
        cancelled
    }
}
