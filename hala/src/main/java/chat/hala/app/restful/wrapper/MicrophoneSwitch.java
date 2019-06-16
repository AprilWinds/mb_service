package chat.hala.app.restful.wrapper;

/**
 * Created by astropete on 2018/4/10.
 */
public class MicrophoneSwitch {

    private Integer microphoneNumber;
    private Boolean switching;

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
}
