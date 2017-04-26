package poc.openshift.greetme.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import poc.openshift.greetme.server.util.PreconditionNotFulfilledException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestControllerExceptionHandler {

    private static final String ERROR_MESSAGE_ATTRIBUTE = "error_message";
    private static final String ERROR_ID_ATTRIBUTE = "error_id";

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(Throwable throwable) {
        return createAndLogErrors("Internal server error", throwable);
    }

    @ExceptionHandler(PreconditionNotFulfilledException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlePreconditionNotFulfilledException(PreconditionNotFulfilledException exception) {
        return createAndLogErrors("Precondition not fulfilled", exception);
    }

    private Map<String, String> createAndLogErrors(String errorMessage, Throwable throwable) {
        String errorId = UUID.randomUUID().toString();
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
        errors.put(ERROR_ID_ATTRIBUTE, errorId);
        log.error("Responding with " + ERROR_ID_ATTRIBUTE + " '" + errorId + "', caused by:", throwable);
        return errors;
    }
}