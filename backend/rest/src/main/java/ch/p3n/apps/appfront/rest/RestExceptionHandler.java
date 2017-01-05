package ch.p3n.apps.appfront.rest;

import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST exception handler.
 *
 * @author deluc1
 * @author zempm3
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MissingClientPublicKeyException.class,
            MissingClientIdException.class,
            MissingInterestIdException.class,
            InvalidActivityNameException.class,
            InvalidClientPublicKeyException.class,
            InvalidClientPushTokenException.class,
            InvalidClientIdException.class,
            InvalidInterestIdException.class,
            InvalidRequestEncryptionException.class
    })
    public String handleHttpBadRequest(final BusinessException e) {
        return e.toString();
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoRegistrationFoundException.class,
            NoActivationFoundException.class
    })
    public String handleHttpNotFound(final BusinessException e) {
        return e.toString();
    }

}
