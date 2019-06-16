package chat.hala.app.service.exception;

/**
 * Created by astropete on 2018/3/20.
 */
public class ObjectIllegalException extends Exception {
    private String message;
    public ObjectIllegalException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
