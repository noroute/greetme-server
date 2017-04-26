package poc.openshift.greetme.server.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
public class Preconditions {

    private Preconditions() {
    }

    public static <T> T checkNotNull(T reference, String referenceName) {
        if (reference == null) {
            throw createAndLogException(referenceName + " may not be null");
        }
        return reference;
    }

    public static String checkNotEmpty(String s, String referenceName) {
        checkNotNull(s, referenceName);
        if (s.trim().isEmpty()) {
            throw createAndLogException(referenceName + " may not be empty");
        }
        return s;
    }

    public static String checkLanguage(String isoLanguageCode, String referenceName) {
        checkNotEmpty(isoLanguageCode, referenceName);
        if (!Arrays.asList(Locale.getISOLanguages()).contains(isoLanguageCode)) {
            String msg = referenceName + " " + isoLanguageCode + " must be one returned by Locale.getISOLanguages()";
            throw createAndLogException(msg);
        }
        return isoLanguageCode;
    }

    private static PreconditionNotFulfilledException createAndLogException(String message) {
        PreconditionNotFulfilledException exception = new PreconditionNotFulfilledException(message);
        log.error(message, exception);
        return exception;
    }
}
