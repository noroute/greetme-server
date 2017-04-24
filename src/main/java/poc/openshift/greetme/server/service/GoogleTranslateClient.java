package poc.openshift.greetme.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import poc.openshift.greetme.server.util.Preconditions;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class GoogleTranslateClient {

    private static final String BASE_URL_TEMPLATE = "%s";
    private static final String SOURCE_LANGUAGE_TEMPLATE = "%s";
    private static final String TARGET_LANGUAGE_TEMPLATE = "%s";
    private static final String TEXT_TO_TRANSLATE_TEMPLATE = "%s";

    private static final String TRANSLATE_URL_TEMPLATE = BASE_URL_TEMPLATE +
            "/translate_a/single" +
            "?client=gtx" +
            "&sl=" + SOURCE_LANGUAGE_TEMPLATE +
            "&tl=" + TARGET_LANGUAGE_TEMPLATE +
            "&dt=t" +
            "&q=" + TEXT_TO_TRANSLATE_TEMPLATE;

    private RestTemplate client = new RestTemplate();
    private String translateBaseUrl = "https://translate.googleapis.com";

    public GoogleTranslateClient() {
    }

    // For tests only
    GoogleTranslateClient(String googleTranslateBaseUrl) {
        translateBaseUrl = googleTranslateBaseUrl;
    }

    public String translate(String text, String sourceLanguage, String targetLanguage) {
        Preconditions.checkNotEmpty(text, "text");
        Preconditions.checkLanguage(sourceLanguage, "sourceLanguage");
        Preconditions.checkLanguage(targetLanguage, "targetLanguage");

        URI url = createUrl(text, sourceLanguage, targetLanguage);
        RequestEntity<Void> requestEntity = RequestEntity.get(url).header(HttpHeaders.USER_AGENT, "unknown").build();
        List responseBody = client.exchange(requestEntity, List.class).getBody();
        String translatedText = (String) ((List) ((List) responseBody.get(0)).get(0)).get(0);
        return translatedText;
    }

    private URI createUrl(String text, String sourceLanguage, String targetLanguage) {
        String urlString = String.format(TRANSLATE_URL_TEMPLATE, translateBaseUrl, sourceLanguage, targetLanguage, encodeText(text));
        try {
            return new URI(urlString);
        }
        catch (URISyntaxException e) {
            String msg = "Google Translate URL " + urlString + " could not be parsed";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private String encodeText(String text) {
        final String encodingScheme = StandardCharsets.UTF_8.name();
        try {
            return URLEncoder.encode(text, encodingScheme);
        }
        catch (UnsupportedEncodingException e) {
            String msg = "Could not URL-encode text " + text + " using " + encodingScheme;
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
