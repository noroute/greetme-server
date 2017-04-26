package poc.openshift.greetme.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poc.openshift.greetme.server.util.Preconditions;

import java.util.Locale;

@Service
public class GreetingService {

    private static final String ENGLISH = Locale.ENGLISH.getLanguage();

    private static final String HELLO_TEMPLATE = "%s";
    private static final String NAME_TEMPLATE = "%s";
    private static final String GREETING_TEMPLATE = HELLO_TEMPLATE + ", " + NAME_TEMPLATE + "!";

    private GoogleTranslateClient googleTranslateClient;

    @Autowired
    public GreetingService(GoogleTranslateClient googleTranslateClient) {
        this.googleTranslateClient = googleTranslateClient;
    }

    public String sayHelloTo(String name, String isoLanguageCode) {
        Preconditions.checkNotEmpty(name, "name");
        Preconditions.checkLanguage(isoLanguageCode, "isoLanguageCode");

        String hello = isoLanguageCode.equals(ENGLISH) ? "Hello" : translateHello(isoLanguageCode);
        return String.format(GREETING_TEMPLATE, hello, name);
    }

    private String translateHello(String isoLanguageCode) {
        return googleTranslateClient.translate("Hello", ENGLISH, isoLanguageCode);
    }
}
