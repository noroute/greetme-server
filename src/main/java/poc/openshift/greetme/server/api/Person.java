package poc.openshift.greetme.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @Getter
    @Setter
    private String name;
}
