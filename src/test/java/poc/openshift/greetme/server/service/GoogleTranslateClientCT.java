package poc.openshift.greetme.server.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

public class GoogleTranslateClientCT {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private GoogleTranslateClient googleTranslateClient = new GoogleTranslateClient("http://localhost:8080");

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
        String translatedText = googleTranslateClient.translate("Das Küken", Locale.GERMAN.getLanguage(), Locale.ENGLISH.getLanguage());

        // then
        verify(getRequestedFor(urlEqualTo(translateUrl)));
        assertThat(translatedText).isEqualTo("The chick");
    }
}