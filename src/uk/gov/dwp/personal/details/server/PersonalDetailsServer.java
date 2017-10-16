package uk.gov.dwp.personal.details.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.cxf.server.CxfBusConfiguration;
import uk.gov.dwp.personal.details.server.dao.mongo.spring.MongoDaoConfig;
import uk.gov.dwp.personal.details.server.resource.configuration.PersonalDetailsResourceConfiguration;

@SpringBootApplication
@Import({
        PropertyPlaceholderAutoConfiguration.class,
        CxfBusConfiguration.class,
        HealthCheckConfiguration.class,
        ApplicationListenerConfiguration.class,
        MongoDaoConfig.class,
        PersonalDetailsResourceConfiguration.class
//        EmbeddedServletContainerAutoConfiguration.class
})
@EnableAutoConfiguration(
        exclude = {
                MongoAutoConfiguration.class,
                SecurityAutoConfiguration.class,
        }
)
public class PersonalDetailsServer {

    public static void main(String[] args) {
        SpringApplication.run(PersonalDetailsServer.class, args);
    }
}