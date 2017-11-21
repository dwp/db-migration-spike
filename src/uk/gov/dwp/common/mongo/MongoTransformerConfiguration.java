package uk.gov.dwp.common.mongo;

import org.bson.BSON;
import org.bson.Transformer;
import uk.gov.dwp.common.id.Id;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

public class MongoTransformerConfiguration {

    public static void registerTransformers() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
        BSON.addDecodingHook(LocalDateTime.class, new LocalDateTimeTransformer());
        BSON.addEncodingHook(LocalDateTime.class, new LocalDateTimeTransformer());
        BSON.addDecodingHook(Instant.class, new InstantTransformer());
        BSON.addEncodingHook(Instant.class, new InstantTransformer());
        BSON.addEncodingHook(Id.class, new IdTransformer());
        BSON.addDecodingHook(Date.class, new InstantTransformer());
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
