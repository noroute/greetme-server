package poc.openshift.greetme.server.service;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GreetingService {

    private static final String GREETING_TEMPLATE = "Hello, %s!";

    public String sayHelloTo(String name) {
        Objects.requireNonNull(name, "name");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name " + name + " may not be empty");
        }
        return String.format(GREETING_TEMPLATE, name);
    }
}
