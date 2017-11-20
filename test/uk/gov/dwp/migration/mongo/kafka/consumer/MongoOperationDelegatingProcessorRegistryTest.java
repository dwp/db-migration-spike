package uk.gov.dwp.migration.mongo.kafka.consumer;

import org.junit.Test;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MongoOperationDelegatingProcessorRegistryTest {

    private final MongoOperationDelegatingProcessor mongoOperationDelegatingProcessor = mock(MongoOperationDelegatingProcessor.class);
    private final MongoOperation mongoOperation = mock(MongoOperation.class);

    private final MongoOperationDelegatingProcessorRegistry underTest = new MongoOperationDelegatingProcessorRegistry();

    @Test
    public void newProcessorIsUsedWhenMessageProcessed() throws Exception {
        assertThat(underTest.add(mongoOperationDelegatingProcessor), is(sameInstance(mongoOperationDelegatingProcessor)));

        underTest.process(mongoOperation);

        verify(mongoOperationDelegatingProcessor).process(mongoOperation);
    }
}
