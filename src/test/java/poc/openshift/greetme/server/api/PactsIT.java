package poc.openshift.greetme.server.api;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.Lifecycle;
import org.springframework.http.MediaType;
import poc.openshift.greetme.server.GreetMeServerApplication;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.util.Locale;

import static org.junit.Assert.fail;

@RunWith(PactRunner.class)
@Provider("greetme_server_provider")
@PactFolder("pacts")
public class PactsIT {

    private static final int PORT = 10080;

    private static boolean thisTestControlsSpringApplicationLifecycle = false;
    private static Lifecycle applicationLifecycle;

    @TestTarget
    public final Target target = new HttpTarget(PORT);

    // How to do it better with SpringClassRule, SpringMethodRule, and @SpringBootTest?
    // Alternatively, try https://github.com/realestate-com-au/pact-jvm-provider-spring-mvc
    @BeforeClass
    public static void startSpring() {
        thisTestControlsSpringApplicationLifecycle = !isSpringAlreadyRunning();
        if (thisTestControlsSpringApplicationLifecycle) {
            applicationLifecycle = SpringApplication.run(GreetMeServerApplication.class);
        }
    }

    private static boolean isSpringAlreadyRunning() {
        Response response;
        try {
            response = greetingsRequest().get();
        }
        catch (Exception e) {
            return false;
        }
        return response.getStatusInfo().equals(Response.Status.OK);
    }

    private static Invocation.Builder greetingsRequest() {
        return ClientBuilder.newClient().target("http://localhost:" + PORT).path("/greetings").request();
    }

    @State("at_least_one_greeting")
    public void toAtLeastOneGreetingState() throws Exception {
        postPersonToGreet("Alice", Locale.ENGLISH.getLanguage());
    }

    private void postPersonToGreet(String name, String nativeLanguageCode) {
        Person person = new Person();
        person.setName(name);
        person.setNativeLanguageCode(nativeLanguageCode);

        Response response = greetingsRequest().post(Entity.entity(person, MediaType.APPLICATION_JSON_UTF8_VALUE));
        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            fail("Greeting for POSTed person not created; server response is: " + response);
        }
    }

    @AfterClass
    public static void stopSpring() {
        if (thisTestControlsSpringApplicationLifecycle) {
            applicationLifecycle.stop();
        }
    }
}