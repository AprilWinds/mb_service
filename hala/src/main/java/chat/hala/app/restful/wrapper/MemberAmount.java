package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Member;

/**
 * Created by astropete on 2018/7/15.
 */
public class MemberAmount {

    private Integer amount;
    private Member member;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
