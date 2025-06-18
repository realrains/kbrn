# KBRN

[![CI](https://github.com/realrains/kbrn/actions/workflows/ci.yml/badge.svg)](https://github.com/realrains/kbrn/actions/workflows/ci.yml)
[![Publish](https://github.com/realrains/kbrn/actions/workflows/publish.yml/badge.svg)](https://github.com/realrains/kbrn/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.realrains.kbrn/kbrn.svg)](https://search.maven.org/artifact/io.github.realrains.kbrn/kbrn)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

***English** | [한국어](README.md)*

## Overview

**kbrn** _(Korean Business Registration Number)_ is a lightweight Java library for safely and efficiently handling 10-digit Korean business registration numbers.

### Key Features

- **Checksum-based Validation** - Accurately validates business registration numbers
- **Automatic Format Conversion** - Seamless conversion between plain (1208147521) and delimited (120-81-47521) formats
- **Immutable Object Design** - Thread-safe and stable value objects
- **Convenient Utilities** - Quick validation and conversion without object creation

## Getting Started

### Requirements

- Java 11 or higher

### Installation

#### Maven
```xml
<dependency>
  <groupId>io.github.realrains.kbrn</groupId>
  <artifactId>kbrn</artifactId>
  <version>0.0.3</version>
</dependency>
```

#### Gradle
```groovy
dependencies {
    implementation 'io.github.realrains.kbrn:kbrn:0.0.3'
}
```

### Jackson Support (Optional)

For JSON serialization/deserialization with Jackson:

#### Maven
```xml
<dependency>
  <groupId>io.github.realrains.kbrn</groupId>
  <artifactId>kbrn-jackson</artifactId>
  <version>0.0.3</version>
</dependency>
```

#### Gradle
```groovy
dependencies {
    implementation 'io.github.realrains.kbrn:kbrn-jackson:0.0.3'
}
```

## Usage

### Basic Validation

```java
import io.github.realrains.kbrn.KbrnUtils;

// Validate business registration numbers
boolean valid = KbrnUtils.isValid("1208147521");    // true
boolean valid2 = KbrnUtils.isValid("120-81-47521"); // true
boolean invalid = KbrnUtils.isValid("1234567890");  // false
```

### Working with KBRN Objects

#### Creating Objects

```java
import io.github.realrains.kbrn.KBRN;

// Create objects from valid business registration numbers
KBRN kbrn1 = KBRN.valueOf("1208147521");
KBRN kbrn2 = KBRN.valueOf("120-81-47521");

// Both formats represent the same business registration number
System.out.println(kbrn1.equals(kbrn2)); // true
```

#### Format Conversion

```java
KBRN kbrn = KBRN.valueOf("1208147521");

// Output in various formats
String plain = kbrn.plainValue();      // "1208147521"
String delimited = kbrn.delimitedValue(); // "120-81-47521"

// toString() returns debugging representation
System.out.println(kbrn); // "KBRN{'120-81-47521'}"
```

#### Extracting Components

```java
KBRN kbrn = KBRN.valueOf("120-81-47521");

// Business registration number components
String prefix = kbrn.serialPrefix();              // "120" (first 3 digits of serial number)
String entityTypeCode = kbrn.businessEntityTypeCode(); // "81" (2-digit business entity type code)
String suffix = kbrn.serialSuffix();              // "47521" (last 5 digits of serial number)

// Checksum-related
String body = kbrn.body();    // "120814752" (first 9 digits used for checksum calculation)
char checksum = kbrn.checksum(); // '1' (last verification digit)
```

### Utility Methods

#### Format Conversion

```java
// Add/remove delimiters
String delimited = KbrnUtils.toDelimitedFormat("1208147521");   // "120-81-47521"
String plain = KbrnUtils.toPlainFormat("120-81-47521");         // "1208147521"
```

#### Checksum Calculation

```java
// Calculate checksum from first 9 digits
char checksum = KbrnUtils.checksumOf("120814752"); // '1'

// Validate checksum only
boolean validChecksum = KbrnUtils.hasValidChecksum("1208147521"); // true
```

#### Format-specific Validation

```java
// Accept all formats
boolean valid = KbrnUtils.isValidFormat("120-81-47521");      // true

// Plain format only
boolean plainOnly = KbrnUtils.isValidPlainFormat("1208147521"); // true

// Delimited format only  
boolean delimitedOnly = KbrnUtils.isValidDelimitedFormat("120-81-47521"); // true
```

### Jackson Serialization/Deserialization

With the `kbrn-jackson` module, you can easily convert KBRN objects to/from JSON:

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.realrain.kbrn.jackson.KbrnModule;

// Register KbrnModule with ObjectMapper
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new KbrnModule());

// Serialization
KBRN kbrn = KBRN.valueOf("120-81-47521");
String json = mapper.writeValueAsString(kbrn); // "120-81-47521"

// Deserialization
KBRN deserialized = mapper.readValue("\"1208147521\"", KBRN.class);
```

For Spring Boot applications, you can auto-register with `@Bean`:

```java
@Configuration
public class JacksonConfig {
    @Bean
    public Module kbrnModule() {
        return new KbrnModule();
    }
}
```

## API Documentation

For detailed API documentation, please refer to the [Javadoc](https://javadoc.io/doc/io.github.realrains.kbrn/kbrn).

## Contributing

Issues and pull requests are welcome! Please report bugs or suggest features at [GitHub Issues](https://github.com/realrains/kbrn/issues).

## License

This project is distributed under the [MIT License](LICENSE).
