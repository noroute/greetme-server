package poc.openshift.greetme.server.api;

import lombok.Value;

@Value
public class Greeting {

    private long id;
    private String message;
}
