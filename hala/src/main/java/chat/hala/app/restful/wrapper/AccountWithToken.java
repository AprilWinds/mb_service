package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Member;

/**
 * Created by astropete on 2017/12/25.
 */
public class AccountWithToken {
    private Member member;
    private String accessToken;
    private String behavior;

    public AccountWithToken(Member member, String accessToken, String behavior){
        this.member = member;
        this.accessToken = accessToken;
        this.behavior = behavior;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
