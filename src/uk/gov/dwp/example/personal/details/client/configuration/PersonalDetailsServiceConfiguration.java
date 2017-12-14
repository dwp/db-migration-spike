package uk.gov.dwp.example.personal.details.client.configuration;

import uk.gov.dwp.example.personal.details.client.RandomPersonalDetailsGenerator;
import uk.gov.dwp.example.personal.details.client.create.CreatePersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.create.CreatePersonalDetailsTask;
import uk.gov.dwp.example.personal.details.client.delete.DeletePersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.delete.DeletePersonalDetailsTask;
import uk.gov.dwp.example.personal.details.client.find.FindPersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.find.FindPersonalDetailsTask;
import uk.gov.dwp.example.personal.details.client.update.UpdatePersonalDetailsService;
import uk.gov.dwp.example.personal.details.client.update.UpdatePersonalDetailsTask;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Client;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class PersonalDetailsServiceConfiguration {

    private final PersonalDetailsV2Client personalDetailsClient;
    private final List<PersonalDetailsId> personalDetailsIdRegistry;
    private final Supplier<Optional<PersonalDetailsId>> personalDetailsIdGenerator;

    public PersonalDetailsServiceConfiguration(PersonalDetailsV2Client personalDetailsClient) {
        this.personalDetailsClient = personalDetailsClient;
        this.personalDetailsIdRegistry = Collections.synchronizedList(new ArrayList<>());
        this.personalDetailsIdGenerator = () -> personalDetailsIdRegistry.isEmpty() ? Optional.empty() : Optional.of(personalDetailsIdRegistry.get(nextInt(0, personalDetailsIdRegistry.size() - 1)));
    }


    public CreatePersonalDetailsService createPersonalDetailsService(Duration executionFrequency) {
        return new CreatePersonalDetailsService(
                newSingleThreadScheduledExecutor(),
                new CreatePersonalDetailsTask(personalDetailsClient, personalDetailsIdRegistry),
                executionFrequency
        );
    }

    public DeletePersonalDetailsService deletePersonalDetailsService(Duration executionFrequency) {
        return new DeletePersonalDetailsService(
                newSingleThreadScheduledExecutor(),
                new DeletePersonalDetailsTask(
                        personalDetailsClient,
                        personalDetailsIdRegistry, personalDetailsIdGenerator
                ),
                executionFrequency
        );
    }

    public FindPersonalDetailsService findPersonalDetailsService(Duration executionFrequency) {
        return new FindPersonalDetailsService(
                newSingleThreadScheduledExecutor(),
                new FindPersonalDetailsTask(
                        personalDetailsClient,
                        personalDetailsIdGenerator),
                executionFrequency
        );
    }

    public UpdatePersonalDetailsService updatePersonalDetailsService(Duration executionFrequency) {
        return new UpdatePersonalDetailsService(
                newSingleThreadScheduledExecutor(),
                new UpdatePersonalDetailsTask(
                        personalDetailsClient,
                        personalDetailsIdGenerator,
                        new RandomPersonalDetailsGenerator()
                ),
                executionFrequency
        );
    }

}
