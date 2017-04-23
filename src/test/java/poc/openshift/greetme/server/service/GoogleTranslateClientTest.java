package poc.openshift.greetme.server.service;

import org.junit.Test;

import java.util.Locale;

public class GoogleTranslateClientTest {

    private static final String SOME_TEXT = "some text";
    private static final Locale SOME_SOURCE_LOCALE = Locale.ENGLISH;
    private static final Locale SOME_TARGET_LOCALE = Locale.FRENCH;

    private GoogleTranslateClient googleTranslateClient = new GoogleTranslateClient();

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_text_is_null() throws Exception {
        // given
        String nullText = null;

        // when
        googleTranslateClient.translate(nullText, SOME_SOURCE_LOCALE, SOME_TARGET_LOCALE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throws_IllegalArgumentException_when_text_is_empty() throws Exception {
        // given
        String emptyText = "";

        // when
        googleTranslateClient.translate(emptyText, SOME_SOURCE_LOCALE, SOME_TARGET_LOCALE);
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_source_Locale_is_null() throws Exception {
        // given
        Locale nullSourceLocale = null;

        // when
        googleTranslateClient.translate(SOME_TEXT, nullSourceLocale, SOME_TARGET_LOCALE);
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_target_Locale_is_null() throws Exception {
        // given
        Locale nullTargetLocale = null;

        // when
        googleTranslateClient.translate(SOME_TEXT, SOME_SOURCE_LOCALE, nullTargetLocale);
    }
}