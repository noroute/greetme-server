package poc.openshift.greetme.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
@Slf4j
public class GoogleTranslateClient {

    private static final String TRANSLATE_URL_TEMPLATE = "%s/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";

    private RestTemplate client = new RestTemplate();
    private String translateBaseUrl = "https://translate.googleapis.com";

    public GoogleTranslateClient() {
    }

    // For tests only
    GoogleTranslateClient(String googleTranslateBaseUrl) {
        translateBaseUrl = googleTranslateBaseUrl;
    }

    public String translate(String text, Locale source, Locale target) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");

        URI url = createUrl(text, source, target);
        RequestEntity<Void> requestEntity = RequestEntity.get(url).header(HttpHeaders.USER_AGENT, "unknown").build();
        List responseBody = client.exchange(requestEntity, List.class).getBody();
        String translatedText = (String) ((List) ((List) responseBody.get(0)).get(0)).get(0);
        return translatedText;
    }

    private URI createUrl(String text, Locale source, Locale target) {
        String urlString = String.format(TRANSLATE_URL_TEMPLATE, translateBaseUrl, source.getLanguage(), target.getLanguage(), text);
        try {
            return new URI(urlString);
        }
        catch (URISyntaxException e) {
            String msg = "Google Translate URL " + urlString + " could not be parsed";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
