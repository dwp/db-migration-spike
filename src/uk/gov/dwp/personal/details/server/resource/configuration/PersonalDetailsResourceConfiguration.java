package uk.gov.dwp.personal.details.server.resource.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.cxf.configuration.CxfConfiguration;
import uk.gov.dwp.common.cxf.configuration.ResourceRegistry;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.dao.mongo.spring.MongoDaoConfig;
import uk.gov.dwp.personal.details.server.resource.CreatePersonalDetailsRequestAdapter;
import uk.gov.dwp.personal.details.server.resource.PersonalDetailsResource;
import uk.gov.dwp.personal.details.server.resource.UpdatePersonalDetailsRequestAdapter;

@Configuration
@Import({
        MongoDaoConfig.class,
        CxfConfiguration.class,
})
public class PersonalDetailsResourceConfiguration {

    @Bean
    public PersonalDetailsResource personalDetailsResource(ResourceRegistry resourceRegistry,
                                                           PersonalDetailsDao personalDetailsDao) {
        return resourceRegistry.add(new PersonalDetailsResource(personalDetailsDao, new CreatePersonalDetailsRequestAdapter(), new UpdatePersonalDetailsRequestAdapter()));
    }
}
