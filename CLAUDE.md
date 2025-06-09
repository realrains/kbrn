# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

KBRN is a Java library for handling Korean Business Registration Numbers (사업자등록번호). It provides validation, format conversion, and business entity type classification for 10-digit Korean business registration numbers.

## Development Commands

### Build
```bash
./gradlew build              # Build and run tests
./gradlew clean build        # Clean build
./gradlew build -x test      # Build without tests
```

### Testing
```bash
./gradlew test                                    # Run all tests
./gradlew test --info                            # Run tests with detailed output
./gradlew test --tests "*.KbrnTest"              # Run specific test class
./gradlew test --tests "*.KbrnTest.testMethod"   # Run specific test method
```

### Documentation
```bash
./gradlew javadoc            # Generate Javadoc
./gradlew javadocJar         # Create javadoc JAR
```

### Publishing
```bash
./gradlew publish            # Publish to local staging
./gradlew build publish jreleaserDeploy  # Deploy to Maven Central (requires env vars)
```

## Architecture

### Core Components

1. **KBRN Class**: Immutable value object representing a business registration number
   - Validates format and checksum on creation
   - Provides both plain (1208147521) and delimited (120-81-47521) formats
   - Extracts components: prefix, business type code, suffix, checksum

2. **KbrnUtils**: Static utility methods for validation and conversion
   - Format validation without object creation
   - Checksum calculation and verification
   - Format conversion utilities

3. **BusinessEntityType**: Enum categorizing business types based on 2-digit code
   - Maps business type codes (01-99) to entity categories
   - Distinguishes individual vs corporate, taxable vs tax-exempt

### Testing Approach

- Uses JUnit 5 with custom annotations (`@ValidKbrnSource`, `@InvalidKbrnSource`)
- Test data provided via `ArgumentsProvider` implementations
- Comprehensive test coverage for validation, formatting, and edge cases
- Sample data in `src/test/resources/kbrn_sample.csv`

### Build Configuration

- Multi-module Gradle project (root + kbrn module)
- Java 11 target compatibility, built with JDK 21
- Maven Central publishing via JReleaser
- GitHub Actions CI/CD for automated testing and releases