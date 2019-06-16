package chat.hala.app.restful.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by astropete on 2017/12/25.
 */
@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)  // 406
public class NotAcceptableException extends Exception {
    private String message;
    public NotAcceptableException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
