package chat.hala.app.service.exception;

/**
 * Created by astropete on 2017/12/25.
 */
public class ThirdPartyServiceException extends Exception {
    private String message;
    public ThirdPartyServiceException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
