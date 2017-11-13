package uk.gov.dwp.personal.details.server.dao.mongo.support;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.personal.details.server.dao.mongo.spring.MongoDaoConfig;
import uk.gov.dwp.personal.details.server.dao.mongo.spring.MongoDaoProperties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        MongoDaoProperties.class,
        MongoDaoConfig.class,
        AbstractMongoDaoTest.KafkaProducerConfig.class,
})
@TestPropertySource("/mongo-dao-test.properties")
public abstract class AbstractMongoDaoTest {

    @Autowired
    protected MongoClient mongoClient;
    @Autowired
    protected MongoDaoProperties mongoDaoProperties;
    @Autowired
    protected MongoOperationKafkaMessageDispatcher kafkaMessageDispatcher;
    protected MongoCollection<Document> collection;

    @Before
    public void setUp() {
        collection = mongoClient.getDatabase(mongoDaoProperties.getDbName()).getCollection(getCollectionName());
        collection.deleteMany(new Document());
        reset(kafkaMessageDispatcher);
    }

    @After
    public void tearDown() {
        collection.deleteMany(new Document());
    }

    protected abstract String getCollectionName();

    @Configuration
    public static class KafkaProducerConfig {

        @Bean
        public MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher() {
            return mock(MongoOperationKafkaMessageDispatcher.class);
        }
    }

}
