package poc.openshift.greetme.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import poc.openshift.greetme.server.validation.ISO639LanguageCode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @Getter
    @Setter
    @NotEmpty
    private String name;

    @Getter
    @Setter
    @ISO639LanguageCode
    private String nativeLanguageCode;
}
