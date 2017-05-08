package poc.openshift.greetme.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GreetingServiceTest {

    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String FRENCH = Locale.FRENCH.getLanguage();

    @Mock
    private GoogleTranslateClient googleTranslateClientMock;

    @InjectMocks
    private GreetingService greetingService;

    @Test
    public void greets_with_hello_bob_if_name_is_bob_and_language_is_english() {
        // when
        String message = greetingService.sayHelloTo("Bob", ENGLISH);

        // then
        assertThat(message).isEqualTo("Hello, Bob!");
    }

    @Test
    public void translates_using_Google_when_language_is_non_english() throws Exception {
        // when
        greetingService.sayHelloTo("Mallory", FRENCH);

        // then
        verify(googleTranslateClientMock).translate("Hello", ENGLISH, FRENCH);
    }
}