package poc.openshift.greetme.server.service;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private static final String GREETING_TEMPLATE = "Hello, %s!";

    public String sayHelloTo(String name) {
        Validate.notEmpty(name);
        return String.format(GREETING_TEMPLATE, name);
    }
}
