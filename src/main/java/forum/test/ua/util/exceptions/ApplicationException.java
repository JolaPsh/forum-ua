package forum.test.ua.util.exceptions;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
