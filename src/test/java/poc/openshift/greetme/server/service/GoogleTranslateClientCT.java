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
    public void translates_from_English_to_French() throws Exception {
        // given
        final String translateUrl = "/translate_a/single?client=gtx&sl=en&tl=fr&dt=t&q=Hello";
        givenThat(get(translateUrl)
                .withHeader(HttpHeaders.USER_AGENT, equalTo("unknown"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("[[[\"Bonjour\",\"Hello\",null,null,0]],null,\"de\"]")));

        // when
        String translatedText = googleTranslateClient.translate("Hello", Locale.ENGLISH, Locale.FRENCH);

        // then
        verify(getRequestedFor(urlEqualTo(translateUrl)));
        assertThat(translatedText).isEqualTo("Bonjour");
    }
}