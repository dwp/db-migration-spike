package uk.gov.dwp.migration.mongo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "source.mongo")
public class SourceMongoDaoProperties extends MongoDaoProperties {
}
