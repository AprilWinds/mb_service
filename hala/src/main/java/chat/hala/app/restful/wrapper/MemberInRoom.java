package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Member;

/**
 * Created by astropete on 2018/5/5.
 */
public class MemberInRoom {

    private Member member;
    private Boolean inout;
    private Integer spent;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Boolean getInout() {
        return inout;
    }

    public void setInout(Boolean inout) {
        this.inout = inout;
    }

    public Integer getSpent() {
        return spent;
    }

    public void setSpent(Integer spent) {
        this.spent = spent;
    }
}
