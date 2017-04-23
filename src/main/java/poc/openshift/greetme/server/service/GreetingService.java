package poc.openshift.greetme.server.service;

import org.springframework.stereotype.Service;
import poc.openshift.greetme.server.util.Preconditions;

@Service
public class GreetingService {

    private static final String GREETING_TEMPLATE = "Hello, %s!";

    public String sayHelloTo(String name) {
        Preconditions.checkNotEmpty(name, "name");
        return String.format(GREETING_TEMPLATE, name);
    }
}
