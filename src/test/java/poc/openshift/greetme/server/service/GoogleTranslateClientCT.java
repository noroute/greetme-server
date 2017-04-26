package poc.openshift.greetme.server.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

public class GoogleTranslateClientCT {

    private static final String DZONGKHA = "dz";
    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String GERMAN = Locale.GERMAN.getLanguage();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private GoogleTranslateClient googleTranslateClient = new GoogleTranslateClient("http://localhost:8080");

    @Before
    public void resetWireMock() {
        wireMockRule.resetAll();
    }

    @Test
    public void translates_from_German_to_English() throws Exception {
        // given
        final String translateUrl = "/translate_a/single?client=gtx&sl=de&tl=en&dt=t&q=Das+K%C3%BCken"; // Das Küken
        givenThat(get(translateUrl)
                .withHeader(HttpHeaders.USER_AGENT, equalTo("unknown"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("[[[\"The chick\",\"Das Küken\",null,null,0]],null,\"de\"]")));

        // when
        String translatedText = googleTranslateClient.translate("Das Küken", GERMAN, ENGLISH);

        // then
        verify(getRequestedFor(urlEqualTo(translateUrl)));
        assertThat(translatedText).isEqualTo("The chick");
    }

    @Test
    public void cannot_translate_to_Dzongkha() throws Exception {
        // given
        final String translateUrl = "/translate_a/single?client=gtx&sl=en&tl=dz&dt=t&q=Hello";
        givenThat(get(translateUrl)
                .withHeader(HttpHeaders.USER_AGENT, equalTo("unknown"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("[null,null,\"en\"]")));

        // when
        String translatedText = googleTranslateClient.translate("Hello", ENGLISH, DZONGKHA);

        // then
        verify(getRequestedFor(urlEqualTo(translateUrl)));
        assertThat(translatedText).isEqualTo("Sorry, could not translate \"Hello\" to Dzongkha");
    }
}