# KBRN

[![CI](https://github.com/realrains/kbrn/actions/workflows/ci.yml/badge.svg)](https://github.com/realrains/kbrn/actions/workflows/ci.yml)
[![Publish](https://github.com/realrains/kbrn/actions/workflows/publish.yml/badge.svg)](https://github.com/realrains/kbrn/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.realrains.kbrn/kbrn.svg)](https://search.maven.org/artifact/io.github.realrains.kbrn/kbrn)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

*[English](README_EN.md) | **한국어***

## 개요

**kbrn** _(Korean Business Registration Number)_ 은 한국의 10자리 사업자등록번호를 안전하고 효율적으로 처리하기 위한 경량 Java 라이브러리입니다. 

### 주요 기능

- **체크섬 기반 유효성 검증** - 사업자등록번호의 유효성을 정확하게 검증
- **형식 자동 변환** - 일반 형식(1208147521)과 구분자 형식(120-81-47521) 간 자유로운 변환
- **불변 객체 설계** - 스레드 안전성과 안정적인 값 객체 제공
- **간편한 유틸리티** - 객체 생성 없이 빠른 검증 및 변환 가능

## 시작하기

### 요구사항

- Java 11 이상

### 설치

#### Maven
```xml
<dependency>
  <groupId>io.github.realrains.kbrn</groupId>
  <artifactId>kbrn</artifactId>
  <version>0.0.2</version>
</dependency>
```

#### Gradle
```groovy
dependencies {
    implementation 'io.github.realrains.kbrn:kbrn:0.0.2'
}
```

## 사용법

### 기본 검증

```java
import io.github.realrains.kbrn.KbrnUtils;

// 사업자등록번호 유효성 검증
boolean valid = KbrnUtils.isValid("1208147521");    // true
boolean valid2 = KbrnUtils.isValid("120-81-47521"); // true
boolean invalid = KbrnUtils.isValid("1234567890");  // false
```

### KBRN 객체 사용

#### 객체 생성

```java
import io.github.realrains.kbrn.KBRN;

// 유효한 사업자등록번호로 객체 생성
KBRN kbrn1 = KBRN.valueOf("1208147521");
KBRN kbrn2 = KBRN.valueOf("120-81-47521");

// 두 형식 모두 동일한 사업자등록번호를 나타냄
System.out.println(kbrn1.equals(kbrn2)); // true
```

#### 형식 변환

```java
KBRN kbrn = KBRN.valueOf("1208147521");

// 다양한 형식으로 출력
String plain = kbrn.plainValue();      // "1208147521"
String delimited = kbrn.delimitedValue(); // "120-81-47521"

// toString()은 디버깅용 표현 반환
System.out.println(kbrn); // "KBRN{'120-81-47521'}"
```

#### 구성 요소 추출

```java
KBRN kbrn = KBRN.valueOf("120-81-47521");

// 사업자등록번호 구성 요소
String prefix = kbrn.serialPrefix();              // "120" (일련번호 앞 3자리)
String entityTypeCode = kbrn.businessEntityTypeCode(); // "81" (사업자 유형 코드 2자리)
String suffix = kbrn.serialSuffix();              // "47521" (일련번호 뒤 5자리)

// 체크섬 관련
String body = kbrn.body();    // "120814752" (체크섬 계산에 사용되는 앞 9자리)
char checksum = kbrn.checksum(); // '1' (마지막 검증 숫자)
```

### 유틸리티 메서드

#### 형식 변환

```java
// 구분자 추가/제거
String delimited = KbrnUtils.toDelimitedFormat("1208147521");   // "120-81-47521"
String plain = KbrnUtils.toPlainFormat("120-81-47521");         // "1208147521"
```

#### 체크섬 계산

```java
// 사업자등록번호 앞 9자리로 체크섬 계산
char checksum = KbrnUtils.checksumOf("120814752"); // '1'

// 체크섬 유효성만 검증
boolean validChecksum = KbrnUtils.hasValidChecksum("1208147521"); // true
```

#### 형식별 검증

```java
// 전체 형식 허용
boolean valid = KbrnUtils.isValidFormat("120-81-47521");      // true

// 일반 형식만 허용
boolean plainOnly = KbrnUtils.isValidPlainFormat("1208147521"); // true

// 구분자 형식만 허용  
boolean delimitedOnly = KbrnUtils.isValidDelimitedFormat("120-81-47521"); // true
```

## API 문서

상세한 API 문서는 [Javadoc](https://javadoc.io/doc/io.github.realrains.kbrn/kbrn) 을 참조하세요.

## 기여하기

이슈 리포트와 풀 리퀘스트를 환영합니다! [GitHub Issues](https://github.com/realrains/kbrn/issues) 에서 버그 리포트나 기능 제안을 해주세요.

## 라이선스

이 프로젝트는 [MIT 라이선스](LICENSE) 하에 배포됩니다.
