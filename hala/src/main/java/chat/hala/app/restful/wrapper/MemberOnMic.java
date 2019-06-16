package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Member;

/**
 * Created by astropete on 2018/4/10.
 */
public class MemberOnMic {

    private Integer microphoneNumber;
    private Boolean switching;
    private Member member;

    public Integer getMicrophoneNumber() {
        return microphoneNumber;
    }

    public void setMicrophoneNumber(Integer microphoneNumber) {
        this.microphoneNumber = microphoneNumber;
    }

    public Boolean getSwitching() {
        return switching;
    }

    public void setSwitching(Boolean switching) {
        this.switching = switching;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
