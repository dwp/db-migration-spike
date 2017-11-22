package uk.gov.dwp.common.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;

public class InstantToInt64Codec implements Codec<Instant> {

    @Override
    public Class<Instant> getEncoderClass() {
        return Instant.class;
    }

    @Override
    public void encode(BsonWriter writer, Instant value, EncoderContext encoderContext) {
        writer.writeInt64(value.toEpochMilli());
    }

    @Override
    public Instant decode(BsonReader reader, DecoderContext decoderContext) {
        return Instant.ofEpochMilli(reader.readInt64());
    }
}