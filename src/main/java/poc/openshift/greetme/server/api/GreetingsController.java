package poc.openshift.greetme.server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import poc.openshift.greetme.server.service.GreetingService;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/greetings")
@Slf4j
public class GreetingsController {

    private final AtomicLong greetingCounter = new AtomicLong();
    private final Map<Long, Greeting> idToGreeting = new ConcurrentHashMap<>();
    private final GreetingService greetingService;

    @Autowired
    public GreetingsController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Greeting> postPersonToGreet(@RequestBody Person person) {
        Greeting greeting = createAndStoreGreeting(person);
        URI location = createLocation(greeting);
        log.debug("Created {} at location {}", greeting, location);
        return ResponseEntity.created(location).body(greeting);
    }

    private Greeting createAndStoreGreeting(Person person) {
        Long id = greetingCounter.incrementAndGet();
        String message = greetingService.sayHelloTo(person.getName());
        Greeting greeting = new Greeting(id, message);
        idToGreeting.put(greeting.getId(), greeting);
        return greeting;
    }

    private URI createLocation(Greeting greeting) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment("{id}").buildAndExpand(greeting.getId())
                .toUri();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Greeting> getGreetingOfId(@PathVariable Long id) {
        if (!idToGreeting.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        Greeting greeting = idToGreeting.get(id);
        return ResponseEntity.ok().body(greeting);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public Collection<Greeting> getAllGreetings() {
        return Collections.unmodifiableCollection(idToGreeting.values());
    }
}
