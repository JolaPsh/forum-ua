package forum.test.ua.util.exceptions;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

