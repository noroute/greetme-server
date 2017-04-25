package poc.openshift.greetme.server.api;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Collection;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GreetingsControllerIT {

    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String FRENCH = Locale.FRENCH.getLanguage();

    @Autowired
    private TestRestTemplate client;

    @LocalServerPort
    private int port;

    @Test
    public void creates_english_greeting_for_posted_person() throws Exception {
        // when
        ResponseEntity<Greeting> response = postPersonToGreetInEnglish("Luke");

        // then
        long expectedGreetingId = 1;
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(new URI("http://localhost:" + port + "/greetings/" + expectedGreetingId));
        assertThat(response.getBody()).isEqualTo(new Greeting(expectedGreetingId, "Hello, Luke!"));
    }

    @Test
    public void creates_french_greeting_for_posted_person() throws Exception {
        // when
        ResponseEntity<Greeting> response = postPersonToGreetInLanguage("Leia", FRENCH);

        // then
        long expectedGreetingId = 2;
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(new URI("http://localhost:" + port + "/greetings/" + expectedGreetingId));
        assertThat(response.getBody()).isEqualTo(new Greeting(expectedGreetingId, "Bonjour, Leia!"));
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
    public void gets_all_greetings() throws Exception {
        // given
        Greeting greeting = postPersonToGreetInEnglish("Qui-Gon Jinn").getBody();

        // when
        ResponseEntity<Collection<Greeting>> response = client.exchange("/greetings", HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Greeting>>() {
        });

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).contains(greeting);
    }

    @Test
    public void responds_with_not_found_when_non_existent_greeting_is_requested() throws Exception {
        // given
        String urlOfNonExistentGreeting = "/greetings/9999";

        // when
        ResponseEntity<String> response = client.getForEntity(urlOfNonExistentGreeting, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    private ResponseEntity<Greeting> postPersonToGreetInEnglish(String personName) {
        return postPersonToGreetInLanguage(personName, ENGLISH);
    }

    private ResponseEntity<Greeting> postPersonToGreetInLanguage(String personName, String nativeLanguageCode) {
        Person person = new Person();
        person.setName(personName);
        person.setNativeLanguageCode(nativeLanguageCode);
        return client.postForEntity("/greetings", person, Greeting.class);
    }
}