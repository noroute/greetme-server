package poc.openshift.greetme.server.api;

import org.assertj.core.api.Condition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import poc.openshift.greetme.server.exception.ErrorObject;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GreetingsControllerIT {

    private static final String GREETINGS_RESOURCE_URL = "/greetings";

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String FRENCH = Locale.FRENCH.getLanguage();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate client;

    @BeforeClass
    public static void setDefaultLocaleUS() {
        // allows us to check the Bean Validation constraint violations in English
        Locale.setDefault(Locale.US);
    }

    @Test
    public void creates_french_greeting_for_posted_person() throws Exception {
        // when
        ResponseEntity<Greeting> response = postPersonToGreetInLanguage("Leia", FRENCH);

        // then
        long expectedGreetingId = 1;
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("http://localhost:" + port + GREETINGS_RESOURCE_URL + "/" + expectedGreetingId));
        assertThat(response.getBody()).isEqualTo(new Greeting(expectedGreetingId, "Bonjour, Leia!"));
    }

    @Test
    public void responds_with_bad_request_when_invalid_data_is_posted() throws Exception {
        // when
        ResponseEntity<ErrorObject<String>> response = postInvalidData();

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        // and
        ErrorObject<String> errorObject = response.getBody();
        assertThat(errorObject.getErrorMessage()).isEqualTo("Invalid JSON body");
        assertThat(errorObject.getErrorDetails()).startsWith("Required request body is missing");
        assertThat(errorObject.getErrorId()).is(uuid());
    }

    @Test
    public void responds_with_bad_request_when_erroneous_person_is_posted() throws Exception {
        // given
        Person personWithoutNameAndNativeLanguage = new Person();

        // when
        ResponseEntity<ErrorObject<List<String>>> response = postErroneousPerson(personWithoutNameAndNativeLanguage);

        // then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        // and
        ErrorObject<List<String>> errorObject = response.getBody();
        assertThat(errorObject.getErrorMessage()).isEqualTo("Validation failed");
        assertThat(errorObject.getErrorDetails()).contains("name may not be empty", "nativeLanguageCode must be an ISO 639 language code");
        assertThat(errorObject.getErrorId()).is(uuid());
    }

    @Test
    public void gets_greeting_of_id() throws Exception {
        // given
        ResponseEntity<Greeting> responseForPost = postPersonToGreetInEnglish("Obi Wan");
        URI location = responseForPost.getHeaders().getLocation();

        // when
        ResponseEntity<Greeting> response = client.getForEntity(location, Greeting.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(responseForPost.getBody());
    }

    @Test
    public void responds_with_not_found_when_non_existent_greeting_is_requested() throws Exception {
        // given
        String urlOfNonExistentGreeting = GREETINGS_RESOURCE_URL + "/9999";

        // when
        ResponseEntity<String> response = client.getForEntity(urlOfNonExistentGreeting, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void gets_all_greetings() throws Exception {
        // given
        Greeting greeting = postPersonToGreetInEnglish("Qui-Gon Jinn").getBody();

        // when
        ResponseEntity<Collection<Greeting>> response = client.exchange(GREETINGS_RESOURCE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Greeting>>() {
        });

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).contains(greeting);
    }

    @AfterClass
    public static void restoreDefaultLocale() {
        Locale.setDefault(DEFAULT_LOCALE);
    }

    private ResponseEntity<Greeting> postPersonToGreetInEnglish(String personName) {
        return postPersonToGreetInLanguage(personName, ENGLISH);
    }

    private ResponseEntity<Greeting> postPersonToGreetInLanguage(String personName, String nativeLanguageCode) {
        Person person = new Person();
        person.setName(personName);
        person.setNativeLanguageCode(nativeLanguageCode);
        return client.postForEntity(GREETINGS_RESOURCE_URL, person, Greeting.class);
    }

    private ResponseEntity<ErrorObject<String>> postInvalidData() throws Exception {
        RequestEntity<Object> postInvalidDataRequest = RequestEntity.post(URI.create(GREETINGS_RESOURCE_URL)).header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(null);
        return client.exchange(postInvalidDataRequest, new ParameterizedTypeReference<ErrorObject<String>>() {
        });
    }

    private ResponseEntity<ErrorObject<List<String>>> postErroneousPerson(Person erroneousPerson) throws Exception {
        RequestEntity<Person> postErroneousPersonRequest = RequestEntity.post(URI.create(GREETINGS_RESOURCE_URL)).body(erroneousPerson);
        return client.exchange(postErroneousPersonRequest, new ParameterizedTypeReference<ErrorObject<List<String>>>() {
        });
    }

    private Condition<String> uuid() {
        return new Condition<String>() {
            @Override
            public boolean matches(String value) {
                try {
                    UUID.fromString(value);
                    return true;
                }
                catch (IllegalArgumentException e) {
                    return false;
                }
            }
        };
    }
}