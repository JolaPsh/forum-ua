package forum.test.ua.web.rest;

import forum.test.ua.util.exceptions.BadRequestException;
import forum.test.ua.util.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Joanna Pakosh, 06.2020
 */

@RestControllerAdvice(annotations = RestController.class)
public class RestInfoExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(RestInfoExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<UserInfoError> handleGenericNotFoundException(NotFoundException exc, HttpServletRequest req) {
        log.error(exc.getCause() + "at request " + req.getRequestURL());
        UserInfoError error = new UserInfoError();
        error.setErrorMessage(exc.getMessage());
        error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<UserInfoError> handleCustomBadRequestException(BadRequestException exc, HttpServletRequest req) {
        log.error(exc.getCause() + "at request " + req.getRequestURL());
        UserInfoError error = new UserInfoError();
        error.setErrorMessage(exc.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
