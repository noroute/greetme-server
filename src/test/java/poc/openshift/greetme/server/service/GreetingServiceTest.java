package poc.openshift.greetme.server.service;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GreetingServiceTest {

    private GreetingService greetingService = new GreetingService();

    @Test
    public void greets_with_hello_bob_if_name_is_bob() {
        String message = greetingService.sayHelloTo("Bob");
        assertThat(message).isEqualTo("Hello, Bob!");
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_if_name_is_null() {
        greetingService.sayHelloTo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throws_IllegalArgumentException_if_name_is_empty() {
        greetingService.sayHelloTo("");
    }
}