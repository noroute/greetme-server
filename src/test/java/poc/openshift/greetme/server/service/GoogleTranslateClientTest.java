package poc.openshift.greetme.server.service;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class GoogleTranslateClientTest {

    private static final String SOME_TEXT = "some text";
    private static final Locale SOME_SOURCE_LOCALE = Locale.ENGLISH;
    private static final Locale SOME_TARGET_LOCALE = Locale.FRENCH;

    private GoogleTranslateClient googleTranslateClient = new GoogleTranslateClient();

    @Test
    public void throws_NullPointerException_when_text_is_null() throws Exception {
        // given
        String nullText = null;

        // when
        try {
            googleTranslateClient.translate(nullText, SOME_SOURCE_LOCALE, SOME_TARGET_LOCALE);
        }
        // then
        catch (NullPointerException e) {
            assertThat(e.getMessage()).isEqualTo("text");
            return;
        }
        fail("expected NullPointerException with message \"text\"");
    }

    @Test
    public void throws_NullPointerException_when_source_Locale_is_null() throws Exception {
        // given
        Locale nullSourceLocale = null;

        // when
        try {
            googleTranslateClient.translate(SOME_TEXT, nullSourceLocale, SOME_TARGET_LOCALE);
        }
        // then
        catch (NullPointerException e) {
            assertThat(e.getMessage()).isEqualTo("source");
            return;
        }
        fail("expected NullPointerException with message \"source\"");
    }

    @Test
    public void throws_NullPointerException_when_target_Locale_is_null() throws Exception {
        // given
        Locale nullTargetLocale = null;

        // when
        try {
            googleTranslateClient.translate(SOME_TEXT, SOME_SOURCE_LOCALE, nullTargetLocale);
        }
        // then
        catch (NullPointerException e) {
            assertThat(e.getMessage()).isEqualTo("target");
            return;
        }
        fail("expected NullPointerException with message \"target\"");
    }
}