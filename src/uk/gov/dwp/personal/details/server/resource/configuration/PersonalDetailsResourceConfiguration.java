package uk.gov.dwp.personal.details.server.resource.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.cxf.configuration.CxfConfiguration;
import uk.gov.dwp.common.cxf.configuration.ResourceRegistry;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.dao.mongo.spring.MongoDaoConfig;
import uk.gov.dwp.personal.details.server.migration.NameSplitter;
import uk.gov.dwp.personal.details.server.resource.CreatePersonalDetailsRequestAdapter;
import uk.gov.dwp.personal.details.server.resource.PersonalDetailsResource;
import uk.gov.dwp.personal.details.server.resource.PersonalDetailsResponseAdapter;
import uk.gov.dwp.personal.details.server.resource.UpdatePersonalDetailsRequestAdapter;
import uk.gov.dwp.personal.details.server.resource.v2.CreatePersonalDetailsV2RequestAdapter;
import uk.gov.dwp.personal.details.server.resource.v2.PersonalDetailsV2Resource;
import uk.gov.dwp.personal.details.server.resource.v2.PersonalDetailsV2ResponseAdapter;
import uk.gov.dwp.personal.details.server.resource.v2.UpdatePersonalDetailsV2RequestAdapter;

@Configuration
@Import({
        MongoDaoConfig.class,
        CxfConfiguration.class,
})
public class PersonalDetailsResourceConfiguration {

    @Bean
    public PersonalDetailsResource personalDetailsResource(ResourceRegistry resourceRegistry,
                                                           PersonalDetailsDao personalDetailsDao,
                                                           NameSplitter nameSplitter) {
        return resourceRegistry.add(new PersonalDetailsResource(
                personalDetailsDao,
                new PersonalDetailsResponseAdapter(),
                new CreatePersonalDetailsRequestAdapter(nameSplitter),
                new UpdatePersonalDetailsRequestAdapter(nameSplitter)
        ));
    }

    @Bean
    public PersonalDetailsV2Resource personalDetailsV2Resource(ResourceRegistry resourceRegistry,
                                                             PersonalDetailsDao personalDetailsDao) {
        return resourceRegistry.add(new PersonalDetailsV2Resource(
                personalDetailsDao,
                new PersonalDetailsV2ResponseAdapter(),
                new CreatePersonalDetailsV2RequestAdapter(),
                new UpdatePersonalDetailsV2RequestAdapter()
        ));
    }
}
