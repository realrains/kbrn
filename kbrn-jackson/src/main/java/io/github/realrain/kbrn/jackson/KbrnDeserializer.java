package io.github.realrain.kbrn.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.realrains.kbrn.KBRN;

import java.io.IOException;

/**
 * Jackson deserializer for KBRN objects.
 * Deserializes string values to KBRN objects.
 */
public class KbrnDeserializer extends JsonDeserializer<KBRN> {
    
    @Override
    public KBRN deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken currentToken = p.getCurrentToken();
        
        if (currentToken == JsonToken.VALUE_STRING) {
            String value = p.getText();
            if (value == null || value.isEmpty()) {
                return null;
            }
            try {
                return KBRN.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid KBRN value: " + value, e);
            }
        } else if (currentToken == JsonToken.VALUE_NULL) {
            return null;
        } else {
            throw new IOException("Expected string value for KBRN, but got: " + currentToken);
        }
    }
    
    @Override
    public Class<KBRN> handledType() {
        return KBRN.class;
    }
}