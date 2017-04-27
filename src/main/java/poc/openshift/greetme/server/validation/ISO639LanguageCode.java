package poc.openshift.greetme.server.validation;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ISO639LanguageCodeValidator.class)
@Documented
@NotEmpty
@ReportAsSingleViolation
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, CONSTRUCTOR, FIELD, METHOD, PARAMETER})
public @interface ISO639LanguageCode {

    String message() default "{poc.openshift.greetme.server.constraints.ISO639LanguageCode.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
