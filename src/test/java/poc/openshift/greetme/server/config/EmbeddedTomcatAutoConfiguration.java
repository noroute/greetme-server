package poc.openshift.greetme.server.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Makes sure Spring Boot always uses an embedded Tomcat.</p>
 *
 * <p>Without this configuration Jetty will be used because this project uses WireMock, which depends on Jetty.
 * (The Jetty JARs will be first in the classpath causing Spring Boot to auto configure Jetty as server.)</p>
 *
 * <p>Using Jetty causes Pact tests to fail as Pact files generated for Tomcat expect an HTTP Content-Type header of
 * "application/json;charset=UTF-8" (upper case "UTF") whereas Jetty responds with "application/json;charset=utf-8"
 * (lower case "utf").</p>
 */
@Configuration
@AutoConfigureBefore(EmbeddedServletContainerAutoConfiguration.class)
public class EmbeddedTomcatAutoConfiguration {

    @Bean
    TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory();
    }
}