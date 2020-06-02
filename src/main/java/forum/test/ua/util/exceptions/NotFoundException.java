package forum.test.ua.util.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class NotFoundException extends ApplicationException {

    private HttpStatus status;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
