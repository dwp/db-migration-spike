package uk.gov.dwp.migration.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.cxf.server.CxfBusConfiguration;
import uk.gov.dwp.migration.server.resource.configuration.PingResourceConfiguration;

@SpringBootApplication
@Import({
        PropertyPlaceholderAutoConfiguration.class,
        CxfBusConfiguration.class,
        HealthCheckConfiguration.class,
//        ApplicationListenerConfiguration.class,
        MongoDaoConfig.class,
        PingResourceConfiguration.class
//        EmbeddedServletContainerAutoConfiguration.class
})
@EnableAutoConfiguration(
        exclude = {
                MongoAutoConfiguration.class,
                SecurityAutoConfiguration.class,
        }
)
public class MigrationServer {

    public static void main(String[] args) {
        SpringApplication.run(MigrationServer.class, args);
    }
}