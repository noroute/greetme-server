package poc.openshift.greetme.server.util;

public class PreconditionNotFulfilledException extends RuntimeException {

    public PreconditionNotFulfilledException(String message) {
        super(message);
    }
}