package uk.gov.dwp.common.mongo.codec;

import com.mongodb.MongoClient;
import org.bson.BsonType;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.Instant;

import static java.util.Collections.singletonMap;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public final class MongoCodecSupport {

    private MongoCodecSupport() {}

    public static CodecRegistry createCodecRegistry() {
        return CodecRegistries.fromRegistries(
                fromCodecs(new InstantToDateTimeCodec()),
                fromProviders(new DocumentCodecProvider(new BsonTypeClassMap(singletonMap(BsonType.DATE_TIME, Instant.class)))),
                MongoClient.getDefaultCodecRegistry()
        );
    }
}
