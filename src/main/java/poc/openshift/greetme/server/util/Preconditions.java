package poc.openshift.greetme.server.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Preconditions {

    private Preconditions() {
    }

    public static <T> T checkNotNull(T reference, String referenceName) {
        if (reference == null) {
            String msg = referenceName + " may not be null";
            RuntimeException npe = new NullPointerException(msg);
            log.error(msg, npe);
            throw npe;
        }
        return reference;
    }

    public static String checkNotEmpty(String s, String referenceName) {
        checkNotNull(s, referenceName);
        if (s.trim().isEmpty()) {
            String msg = referenceName + " may not be empty";
            RuntimeException iae = new IllegalArgumentException(msg);
            log.error(msg, iae);
            throw iae;
        }
        return s;
    }
}
