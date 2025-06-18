package io.github.realrain.kbrn.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.realrains.kbrn.KBRN;

/**
 * Jackson module for KBRN serialization and deserialization support.
 * 
 * <p>Usage example:</p>
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.registerModule(new KbrnModule());
 * 
 * // Serialization
 * KBRN kbrn = KBRN.valueOf("120-81-47521");
 * String json = mapper.writeValueAsString(kbrn); // "120-81-47521"
 * 
 * // Deserialization
 * KBRN deserialized = mapper.readValue("\"1208147521\"", KBRN.class);
 * }</pre>
 */
public class KbrnModule extends SimpleModule {
    
    private static final String NAME = "KbrnModule";
    
    public KbrnModule() {
        super(NAME, Version.unknownVersion());
        
        addSerializer(KBRN.class, new KbrnSerializer());
        addDeserializer(KBRN.class, new KbrnDeserializer());
    }
}