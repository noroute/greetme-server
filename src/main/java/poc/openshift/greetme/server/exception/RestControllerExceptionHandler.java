package poc.openshift.greetme.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return createAndLogErrorObject("Invalid JSON body", exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errorDetails = createMessagesTellingWhichFieldsFailedHowTheirValidation(exception);
        return createAndLogErrorObject("Validation failed", errorDetails, exception);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorObject<String> handleThrowable(Throwable throwable) {
        return createAndLogErrorObject("Internal server error", throwable);
    }

    private ErrorObject<String> createAndLogErrorObject(String errorMessage, Throwable throwable) {
        return createAndLogErrorObject(errorMessage, throwable.getMessage(), throwable);
    }

    private <T> ErrorObject<T> createAndLogErrorObject(String errorMessage, T errorDetails, Throwable throwable) {
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