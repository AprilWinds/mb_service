package chat.hala.app.service.exception;

public class ObjectAlreadyUpdateException extends Exception {

    private String message;

    public ObjectAlreadyUpdateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
