package uk.gov.dwp.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.dwp.common.id.Id;

import java.io.IOException;

public class IdDeserializer extends JsonDeserializer<Id> {

    @Override
    public Id deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}
