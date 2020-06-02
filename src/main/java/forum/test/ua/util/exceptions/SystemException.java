package forum.test.ua.util.exceptions;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class SystemException extends RuntimeException {

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message){
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
