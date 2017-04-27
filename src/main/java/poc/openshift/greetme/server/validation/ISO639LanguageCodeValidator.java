package poc.openshift.greetme.server.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ISO639LanguageCodeValidator implements ConstraintValidator<ISO639LanguageCode, String> {

    private static final List<String> ISO_639_LANGUAGE_CODES = Arrays.asList(Locale.getISOLanguages());

    @Override
    public void initialize(ISO639LanguageCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ISO_639_LANGUAGE_CODES.contains(value);
    }
}