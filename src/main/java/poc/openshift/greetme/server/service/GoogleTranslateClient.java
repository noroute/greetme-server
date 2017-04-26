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
import java.util.Locale;

@Component
@Slf4j
public class GoogleTranslateClient {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private static final String BASE_URL_TEMPLATE = "%s";
    private static final String SOURCE_LANGUAGE_CODE_TEMPLATE = "%s";
    private static final String TARGET_LANGUAGE_CODE_TEMPLATE = "%s";

    private static final String TEXT_TO_TRANSLATE_TEMPLATE = "%s";
    private static final String TRANSLATE_URL_TEMPLATE = BASE_URL_TEMPLATE +
            "/translate_a/single" +
            "?client=gtx" +
            "&sl=" + SOURCE_LANGUAGE_CODE_TEMPLATE +
            "&tl=" + TARGET_LANGUAGE_CODE_TEMPLATE +
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

    public String translate(String text, String sourceLanguageCode, String targetLanguageCode) {
        Preconditions.checkNotEmpty(text, "text");
        Preconditions.checkLanguage(sourceLanguageCode, "sourceLanguageCode");
        Preconditions.checkLanguage(targetLanguageCode, "targetLanguageCode");

        URI url = createUrl(text, sourceLanguageCode, targetLanguageCode);
        RequestEntity<Void> requestEntity = RequestEntity.get(url).header(HttpHeaders.USER_AGENT, "unknown").build();
        List responseBody = client.exchange(requestEntity, List.class).getBody();
        String translatedText = parseResponseBody(responseBody, text, targetLanguageCode);
        log.info("Translated \"{}\" (language code: {}) to \"{}\" (language code: {})", text, sourceLanguageCode, translatedText, targetLanguageCode);
        return translatedText;
    }

    private URI createUrl(String text, String sourceLanguageCode, String targetLanguageCode) {
        String urlString = String.format(TRANSLATE_URL_TEMPLATE, translateBaseUrl, sourceLanguageCode, targetLanguageCode, encodeText(text));
        try {
            URI uri = new URI(urlString);
            log.debug("Google Translate URL: {}", uri);
            return uri;
        }
        catch (URISyntaxException e) {
            String msg = "Google Translate URL " + urlString + " could not be parsed";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private String encodeText(String text) {
        try {
            return URLEncoder.encode(text, UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            String msg = "Could not URL-encode text " + text + " using " + UTF_8;
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private String parseResponseBody(List responseBody, String text, String targetLanguage) {
        if (isTextNotTranslated(responseBody)) {
            String language = Locale.forLanguageTag(targetLanguage).getDisplayLanguage(Locale.ENGLISH);
            return "Sorry, could not translate \"" + text + "\" to " + language;
        }
        return (String) ((List) ((List) responseBody.get(0)).get(0)).get(0);
    }

    private boolean isTextNotTranslated(List responseBody) {
        return responseBody.get(0) == null;
    }
}