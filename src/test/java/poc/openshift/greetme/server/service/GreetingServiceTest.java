package poc.openshift.greetme.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import poc.openshift.greetme.server.util.Preconditions;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Preconditions.class})
public class GreetingServiceTest {

    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String FRENCH = Locale.FRENCH.getLanguage();

    @Mock
    private GoogleTranslateClient googleTranslateClientMock;

    @InjectMocks
    private GreetingService greetingService;

    @Test
    public void checks_preconditions() throws Exception {
        // given
        mockStatic(Preconditions.class);

        // when
        greetingService.sayHelloTo("Alice", ENGLISH);

        // then
        verifyStatic();
        Preconditions.checkNotEmpty(eq("Alice"), anyString());

        // and
        verifyStatic();
        Preconditions.checkLanguage(eq(ENGLISH), anyString());
    }

    @Test
    public void greets_with_hello_bob_if_name_is_bob_and_language_is_english() {
        // when
        String message = greetingService.sayHelloTo("Bob", ENGLISH);

        // then
        assertThat(message).isEqualTo("Hello, Bob!");
    }

    @Test
    public void greets_with_bonjour_mallory_if_name_is_mallory_and_language_is_french() throws Exception {
        // given
        when(googleTranslateClientMock.translate("Hello", ENGLISH, FRENCH)).thenReturn("Bonjour");

        // when
        String message = greetingService.sayHelloTo("Mallory", FRENCH);

        // then
        assertThat(message).isEqualTo("Bonjour, Mallory!");
    }
}