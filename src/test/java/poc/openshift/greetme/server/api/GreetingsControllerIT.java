package poc.openshift.greetme.server.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingsControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> responseEntity;

    @Test
    public void should_respond_with_not_found_when_non_existent_greeting_is_requested() throws Exception {
        // given
        String urlOfNonExistentGreeting = "/greetings/9999";

        // when
        responseEntity = restTemplate.getForEntity(urlOfNonExistentGreeting, String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }
}