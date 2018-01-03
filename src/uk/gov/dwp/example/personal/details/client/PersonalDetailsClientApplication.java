package uk.gov.dwp.example.personal.details.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.id.Id;
import uk.gov.dwp.common.jackson.ISO8601DateFormatWithMilliSeconds;
import uk.gov.dwp.common.jackson.IdDeserializer;
import uk.gov.dwp.common.jackson.IdSerializer;
import uk.gov.dwp.example.personal.details.client.configuration.PersonalDetailsServiceConfiguration;
import uk.gov.dwp.example.personal.details.client.create.CreatePersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.delete.DeletePersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.find.FindPersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.update.UpdatePersonalDetailsService;
import uk.gov.dwp.personal.details.client.PersonalDetailsClient;

import java.time.Duration;

import static java.util.Collections.singletonList;

public class PersonalDetailsClientApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalDetailsClientApplication.class);
    private static final String DEFAULT_PERSONAL_DETAILS_SERVER_BASE_URL = "http://localhost:8008/personal-details";

    private final CreatePersonalDetailsService createPersonalDetailsService;
    private final DeletePersonalDetailsService deletePersonalDetailsService;
    private final FindPersonalDetailsService findPersonalDetailsService;
    private final UpdatePersonalDetailsService updatePersonalDetailsService;

    public PersonalDetailsClientApplication(CreatePersonalDetailsService createPersonalDetailsService,
                                            DeletePersonalDetailsService deletePersonalDetailsService,
                                            FindPersonalDetailsService findPersonalDetailsService,
                                            UpdatePersonalDetailsService updatePersonalDetailsService) {
        this.createPersonalDetailsService = createPersonalDetailsService;
        this.deletePersonalDetailsService = deletePersonalDetailsService;
        this.findPersonalDetailsService = findPersonalDetailsService;
        this.updatePersonalDetailsService = updatePersonalDetailsService;
    }

    public void start() {
        createPersonalDetailsService.start();
        deletePersonalDetailsService.start();
        findPersonalDetailsService.start();
        updatePersonalDetailsService.start();
    }

    public static void main(String[] args) {
        final String url = resolvePersonalDetailsBaseUrl(args);

        PersonalDetailsClient personalDetailsClient = JAXRSClientFactory.create(
                url,
                PersonalDetailsClient.class,
                singletonList(new JacksonJsonProvider(objectMapper()))
        );
        PersonalDetailsServiceConfiguration personalDetailsServiceConfiguration = new PersonalDetailsServiceConfiguration(personalDetailsClient);

        PersonalDetailsClientApplication personalDetailsClientApplication = new PersonalDetailsClientApplication(
                personalDetailsServiceConfiguration.createPersonalDetailsService(Duration.ofSeconds(2L)),
                personalDetailsServiceConfiguration.deletePersonalDetailsService(Duration.ofSeconds(5L)),
                personalDetailsServiceConfiguration.findPersonalDetailsService(Duration.ofSeconds(1L)),
                personalDetailsServiceConfiguration.updatePersonalDetailsService(Duration.ofSeconds(3L))
        );
        personalDetailsClientApplication.start();
    }

    private static String resolvePersonalDetailsBaseUrl(String[] args) {
        String url = System.getenv("PERSONAL_DETAILS_BASE_URL");
        if (args.length == 1) {
            url = args[0];
            LOGGER.info("Using URL from Command Line: {}", url);
        } else if (url == null) {
            LOGGER.info("Using default URL: {}", DEFAULT_PERSONAL_DETAILS_SERVER_BASE_URL);
            url = DEFAULT_PERSONAL_DETAILS_SERVER_BASE_URL;
        } else {
            LOGGER.info("Using URL from PERSONAL_DETAILS_BASE_URL environment variable: {}", url);
        }
        return url;
    }

    private static ObjectMapper objectMapper() {
            return new ObjectMapper()
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .setDateFormat(new ISO8601DateFormatWithMilliSeconds())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .registerModule(new SimpleModule()
                            .addSerializer(Id.class, new IdSerializer())
                            .addDeserializer(Id.class, new IdDeserializer()))
                    ;
    }
}
