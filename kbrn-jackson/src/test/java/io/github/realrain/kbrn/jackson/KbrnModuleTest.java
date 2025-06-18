package io.github.realrain.kbrn.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.github.realrains.kbrn.KBRN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KbrnModuleTest {
    
    private ObjectMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new KbrnModule());
    }
    
    @Test
    @DisplayName("Should serialize KBRN to string")
    void testSerialization() throws JsonProcessingException {
        KBRN kbrn = KBRN.valueOf("120-81-47521");
        String json = mapper.writeValueAsString(kbrn);
        
        assertThat(json).isEqualTo("\"120-81-47521\"");
    }
    
    @Test
    @DisplayName("Should serialize KBRN without delimiter to plain format")
    void testSerializationPlainFormat() throws JsonProcessingException {
        KBRN kbrn = KBRN.valueOf("1208147521");
        String json = mapper.writeValueAsString(kbrn);
        
        assertThat(json).isEqualTo("\"120-81-47521\"");
    }
    
    @Test
    @DisplayName("Should serialize null KBRN to null")
    void testSerializationNull() throws JsonProcessingException {
        String json = mapper.writeValueAsString((KBRN) null);
        
        assertThat(json).isEqualTo("null");
    }
    
    @Test
    @DisplayName("Should deserialize string to KBRN")
    void testDeserialization() throws JsonProcessingException {
        String json = "\"120-81-47521\"";
        KBRN kbrn = mapper.readValue(json, KBRN.class);
        
        assertThat(kbrn).isNotNull();
        assertThat(kbrn.delimitedValue()).isEqualTo("120-81-47521");
    }
    
    @Test
    @DisplayName("Should deserialize plain format string to KBRN")
    void testDeserializationPlainFormat() throws JsonProcessingException {
        String json = "\"1208147521\"";
        KBRN kbrn = mapper.readValue(json, KBRN.class);
        
        assertThat(kbrn).isNotNull();
        assertThat(kbrn.plainValue()).isEqualTo("1208147521");
    }
    
    @Test
    @DisplayName("Should deserialize null to null KBRN")
    void testDeserializationNull() throws JsonProcessingException {
        String json = "null";
        KBRN kbrn = mapper.readValue(json, KBRN.class);
        
        assertThat(kbrn).isNull();
    }
    
    @Test
    @DisplayName("Should deserialize empty string to null")
    void testDeserializationEmptyString() throws JsonProcessingException {
        String json = "\"\"";
        KBRN kbrn = mapper.readValue(json, KBRN.class);
        
        assertThat(kbrn).isNull();
    }
    
    @Test
    @DisplayName("Should throw exception for invalid KBRN format")
    void testDeserializationInvalidFormat() {
        String json = "\"123-45-67890\"";
        
        assertThatThrownBy(() -> mapper.readValue(json, KBRN.class))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Invalid KBRN value");
    }
    
    @Test
    @DisplayName("Should throw exception for non-string JSON value")
    void testDeserializationNonString() {
        String json = "123";
        
        assertThatThrownBy(() -> mapper.readValue(json, KBRN.class))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Expected string value for KBRN");
    }
    
    @Test
    @DisplayName("Should serialize and deserialize in object context")
    void testSerializationDeserializationInObject() throws JsonProcessingException {
        TestDto dto = new TestDto();
        dto.name = "Test Company";
        dto.kbrn = KBRN.valueOf("120-81-47521");
        
        String json = mapper.writeValueAsString(dto);
        assertThat(json).contains("\"name\":\"Test Company\"");
        assertThat(json).contains("\"kbrn\":\"120-81-47521\"");
        
        TestDto deserialized = mapper.readValue(json, TestDto.class);
        assertThat(deserialized.name).isEqualTo("Test Company");
        assertThat(deserialized.kbrn).isEqualTo(KBRN.valueOf("120-81-47521"));
    }
    
    @Test
    @DisplayName("Should handle null KBRN in object context")
    void testNullKbrnInObject() throws JsonProcessingException {
        TestDto dto = new TestDto();
        dto.name = "Test Company";
        dto.kbrn = null;
        
        String json = mapper.writeValueAsString(dto);
        assertThat(json).contains("\"kbrn\":null");
        
        TestDto deserialized = mapper.readValue(json, TestDto.class);
        assertThat(deserialized.name).isEqualTo("Test Company");
        assertThat(deserialized.kbrn).isNull();
    }
    
    static class TestDto {
        public String name;
        public KBRN kbrn;
    }
}