package uk.gov.dwp.migration.mongo.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.BSON;
import org.bson.Transformer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import uk.gov.dwp.common.id.Id;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.mongodb.MongoCredential.createScramSha1Credential;
import static java.time.ZoneOffset.UTC;
import static java.util.Collections.singletonList;

@Configuration
@EnableConfigurationProperties(SourceMongoDaoProperties.class)
public class MongoDaoConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
        BSON.addDecodingHook(LocalDateTime.class, new LocalDateTimeTransformer());
        BSON.addEncodingHook(LocalDateTime.class, new LocalDateTimeTransformer());
        BSON.addDecodingHook(Instant.class, new InstantTransformer());
        BSON.addEncodingHook(Instant.class, new InstantTransformer());
        BSON.addEncodingHook(Id.class, new IdTransformer());
        BSON.addDecodingHook(Date.class, new InstantTransformer());
    }

    @Bean
    @DependsOn("sourceMongoDaoProperties")
    public MongoClient sourceMongoClient(MongoDaoProperties sourceMongoDaoProperties) {
        return new MongoClient(
                createSeeds(sourceMongoDaoProperties),
                createCredentials(sourceMongoDaoProperties),
                sourceMongoDaoProperties.mongoClientOptions()
        );
    }

    @Bean
    @DependsOn("destinationMongoDaoProperties")
    public MongoClient destinationMongoClient(MongoDaoProperties destinationMongoDaoProperties) {
        return new MongoClient(
                createSeeds(destinationMongoDaoProperties),
                createCredentials(destinationMongoDaoProperties),
                destinationMongoDaoProperties.mongoClientOptions()
        );
    }

    private List<MongoCredential> createCredentials(MongoDaoProperties mongoDaoProperties) {
        return mongoDaoProperties.getCollection().getUsername()
                .map(username -> singletonList(createScramSha1Credential(
                        username,
                        mongoDaoProperties.getDbName(),
                        mongoDaoProperties.getCollection()
                                .getPassword()
                                .orElseThrow(() -> new IllegalArgumentException("Password is required when username specified"))
                                .toCharArray())))
                .orElse(Collections.emptyList());
    }

    private List<ServerAddress> createSeeds(MongoDaoProperties mongoDaoProperties) {
        return mongoDaoProperties.getServerAddresses()
                .stream()
                .map(serverAddress -> new ServerAddress(serverAddress.getHost(), serverAddress.getPort()))
                .collect(Collectors.toList());
    }

    public static class LocalDateTimeTransformer implements Transformer {

        @Override
        public Object transform(Object objectToTransform) {
            if (objectToTransform instanceof LocalDateTime) {
                return Date.from(((LocalDateTime) objectToTransform).toInstant(UTC));
            } else if (objectToTransform instanceof Date) {
                return LocalDateTime.ofInstant(((Date) objectToTransform).toInstant(), UTC);
            }
            throw new IllegalArgumentException("LocalDateTimeTransformer can only be used with LocalDateTime or Date");
        }
    }

    public static class InstantTransformer implements Transformer {

        @Override
        public Object transform(Object objectToTransform) {
            if (objectToTransform instanceof Instant) {
                return Date.from(((Instant) objectToTransform));
            } else if (objectToTransform instanceof Date) {
                return ((Date) objectToTransform).toInstant();
            }
            throw new IllegalArgumentException("InstantTransformer can only be used with Instant or Date");
        }
    }

    public static class IdTransformer implements Transformer {

        @Override
        public Object transform(Object objectToTransform) {
            if (objectToTransform instanceof Id) {
                return ((Id) objectToTransform).getId().toString();
            }
            throw new IllegalArgumentException("IdTransformer can only be used with instances of Id");
        }
    }
}
