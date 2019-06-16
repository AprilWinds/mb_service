package chat.hala.app.restful.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by astropete on 2018/3/8.
 */
@ResponseStatus(value= HttpStatus.PRECONDITION_FAILED)  // 412
public class PreconditionFailedException extends Exception {
    private String message;

    public PreconditionFailedException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
