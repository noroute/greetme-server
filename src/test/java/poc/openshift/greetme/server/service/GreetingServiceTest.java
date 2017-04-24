package poc.openshift.greetme.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import poc.openshift.greetme.server.util.Preconditions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Preconditions.class})
public class GreetingServiceTest {

    private GreetingService greetingService = new GreetingService();

    @Test
    public void checks_preconditions() throws Exception {
        // given
        mockStatic(Preconditions.class);

        // when
        greetingService.sayHelloTo("Alice");

        // then
        verifyStatic();
        Preconditions.checkNotEmpty(eq("Alice"), anyString());
    }

    @Test
    public void greets_with_hello_bob_if_name_is_bob() {
        String message = greetingService.sayHelloTo("Bob");
        assertThat(message).isEqualTo("Hello, Bob!");
    }
}