package chat.hala.app.restful.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by astropete on 2017/12/25.
 */
@ResponseStatus(value= HttpStatus.CONFLICT)  // 409
public class ConflictException extends Exception {
    private String message;
    public ConflictException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
