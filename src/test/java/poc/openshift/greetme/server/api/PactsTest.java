package poc.openshift.greetme.server.api;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;
import poc.openshift.greetme.server.GreetMeServerApplication;

import java.lang.reflect.Field;
import java.util.Map;

@RunWith(PactRunner.class)
@Provider("greetme_server_provider")
@PactFolder("pacts")
@Slf4j
public class PactsTest {

    private static ConfigurableApplicationContext applicationContext;

    @TestTarget
    public final Target target = new HttpTarget(8080);

    // How to do it better with SpringClassRule, SpringMethodRule, and @SpringBootTest?
    // Alternatively, try https://github.com/realestate-com-au/pact-jvm-provider-spring-mvc
    @BeforeClass
    public static void startSpring() {
        applicationContext = SpringApplication.run(GreetMeServerApplication.class);
    }

    @State("at_least_one_greeting")
    public void toAtLeastOneGreetingState() throws Exception {
        Map<Long, Greeting> idToGreeting = addGreetingToGreetingsControllerUsingReflection(999l, "Hello, Alice!");
        log.info("Added greeting to GreetingsController to prepare state 'at_least_one_greeting'. Map 'idToGreeting' now contains: {}", idToGreeting);
    }

    private Map<Long, Greeting> addGreetingToGreetingsControllerUsingReflection(Long greetingId, String greetingMessage) throws Exception {
        Field idToGreetingField = ReflectionUtils.findField(GreetingsController.class, "idToGreeting");
        idToGreetingField.setAccessible(true);
        Object greetingsController = applicationContext.getBean("greetingsController");
        Greeting greeting = new Greeting(greetingId, greetingMessage);
        @SuppressWarnings("unchecked")
        Map<Long, Greeting> idToGreeting = (Map<Long, Greeting>) idToGreetingField.get(greetingsController);
        idToGreeting.put(greeting.getId(), greeting);
        return idToGreeting;
    }

    @AfterClass
    public static void stopSpring() {
        applicationContext.stop();
    }
}