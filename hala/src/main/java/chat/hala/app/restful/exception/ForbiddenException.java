package chat.hala.app.restful.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by astropete on 2018/1/28.
 */
@ResponseStatus(value= HttpStatus.FORBIDDEN) //403
public class ForbiddenException extends Exception {

    private String message;
    public ForbiddenException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
