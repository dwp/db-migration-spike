package uk.gov.dwp.migration.mongo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "destination.mongo")
public class DestinationMongoDaoProperties extends MongoDaoProperties {
}
