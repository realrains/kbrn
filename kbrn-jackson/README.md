# KBRN Jackson Module

Jackson serialization support for Korean Business Registration Numbers (KBRN).

## Installation

### Gradle
```gradle
dependencies {
    implementation 'io.github.realrain:kbrn-jackson:1.0.0'
}
```

### Maven
```xml
<dependency>
    <groupId>io.github.realrain</groupId>
    <artifactId>kbrn-jackson</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Jackson Version Compatibility

This module supports Jackson versions from 2.12.0 to 2.15.x:
- Jackson 2.12.x - Java 7+ required
- Jackson 2.13.x - Java 8+ required
- Jackson 2.14.x - Java 8+ required
- Jackson 2.15.x - Java 8+ required

The module will use whatever Jackson version is available in your project within this range.

## Usage

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.realrain.kbrn.jackson.KbrnModule;
import io.github.realrains.kbrn.KBRN;

// Register the module
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new KbrnModule());

// Serialization
KBRN kbrn = KBRN.valueOf("120-81-47521");
String json = mapper.writeValueAsString(kbrn); // "120-81-47521"

// Deserialization
KBRN deserialized = mapper.readValue("\"1208147521\"", KBRN.class);
```

## Features

- Serializes KBRN objects to delimited format strings (e.g., "120-81-47521")
- Deserializes both plain (1208147521) and delimited (120-81-47521) formats
- Handles null values properly
- Provides clear error messages for invalid KBRN values
- Supports KBRN objects within complex data structures

## Example with Data Transfer Objects

```java
public class Company {
    private String name;
    private KBRN businessNumber;
    
    // getters and setters
}

// Serialization
Company company = new Company();
company.setName("Example Corp");
company.setBusinessNumber(KBRN.valueOf("120-81-47521"));

String json = mapper.writeValueAsString(company);
// {"name":"Example Corp","businessNumber":"120-81-47521"}

// Deserialization
Company deserialized = mapper.readValue(json, Company.class);
```

## License

MIT License