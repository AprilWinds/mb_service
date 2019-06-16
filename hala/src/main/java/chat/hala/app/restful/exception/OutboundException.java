package chat.hala.app.restful.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by astropete on 2017/10/23.
 */
@ResponseStatus(value= HttpStatus.SERVICE_UNAVAILABLE)  // 503
public class OutboundException extends Exception {
    private String message;

    public OutboundException(String reason) {
        this.message = reason;
    }

    @Override
    public String getMessage(){
        return message;
    }
}