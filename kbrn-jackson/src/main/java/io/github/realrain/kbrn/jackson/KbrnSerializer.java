package io.github.realrain.kbrn.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.realrains.kbrn.KBRN;

import java.io.IOException;

/**
 * Jackson serializer for KBRN objects.
 * Serializes KBRN objects to their string representation.
 */
public class KbrnSerializer extends JsonSerializer<KBRN> {
    
    @Override
    public void serialize(KBRN value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.delimitedValue());
        }
    }
    
    @Override
    public Class<KBRN> handledType() {
        return KBRN.class;
    }
}