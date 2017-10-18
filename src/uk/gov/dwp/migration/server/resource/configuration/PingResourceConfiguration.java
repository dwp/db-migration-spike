package uk.gov.dwp.migration.server.resource.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.cxf.configuration.CxfConfiguration;
import uk.gov.dwp.common.cxf.configuration.ResourceRegistry;
import uk.gov.dwp.migration.server.resource.PingResource;

@Configuration
@Import({
        CxfConfiguration.class,
})
public class PingResourceConfiguration {

    @Bean
    public PingResource pingResource(ResourceRegistry resourceRegistry) {
        return resourceRegistry.add(new PingResource());
    }

}
