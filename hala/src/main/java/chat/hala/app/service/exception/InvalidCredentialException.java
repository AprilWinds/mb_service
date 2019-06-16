package chat.hala.app.service.exception;

/**
 * Created by astropete on 2017/12/26.
 */
public class InvalidCredentialException extends Exception {
    private String message;
    public InvalidCredentialException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
