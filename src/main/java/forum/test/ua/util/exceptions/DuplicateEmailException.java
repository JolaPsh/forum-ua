package forum.test.ua.util.exceptions;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class DuplicateEmailException extends SystemException {
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
