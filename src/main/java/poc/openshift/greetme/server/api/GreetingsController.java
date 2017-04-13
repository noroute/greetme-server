package poc.openshift.greetme.server.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
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
        String message = greetingService.greetName(person.getName());

        Long id = greetingCounter.incrementAndGet();
        Greeting greeting = new Greeting(id, message);
        idToGreeting.put(greeting.getId(), greeting);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment("{id}").buildAndExpand(greeting.getId()).toUri();
        log.debug("Created {} at location {}", greeting, location);

        return ResponseEntity.created(location).body(greeting);
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
