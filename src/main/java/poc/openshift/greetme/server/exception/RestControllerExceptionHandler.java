package poc.openshift.greetme.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject<String> handleThrowable(Throwable throwable) {
        String errorDetails = throwable.getMessage();
        return createAndLogError("Internal server error", errorDetails, throwable);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errorDetails = createMessagesTellingWhichFieldsFailedHowTheirValidation(exception);
        return createAndLogError("Validation failed", errorDetails, exception);
    }

    private <T> ErrorObject<T> createAndLogError(String errorMessage, T errorDetails, Throwable throwable) {
        ErrorObject<T> errorObject = new ErrorObject<>(errorMessage, errorDetails);
        log.error("Responding with " + errorObject + ", thrown Exception was:", throwable);
        return errorObject;
    }

    private List<String> createMessagesTellingWhichFieldsFailedHowTheirValidation(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
    }
}