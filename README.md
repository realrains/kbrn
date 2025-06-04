# kbrn

[![CI](https://github.com/realrains/kbrn/actions/workflows/ci.yml/badge.svg)](https://github.com/realrains/kbrn/actions/workflows/ci.yml)
[![Publish](https://github.com/realrains/kbrn/actions/workflows/publish.yml/badge.svg)](https://github.com/realrains/kbrn/actions/workflows/publish.yml)


**kbrn** _(Korean Business Registration Number)_ 은 대한민국의 사업자등록번호를 처리하기 위한 Java 라이브러리입니다.
사업자등록번호의 형식 검증, 체크섬 계산을 통한 유효성 검사, 형식 변환 등을 간편하게 처리할 수 있도록 도와줍니다.

## Requirements

- JDK 11 이상

## Installation

**Maven**

```xml
<dependency>
  <groupId>io.github.realrains.kbrn</groupId>
  <artifactId>kbrn</artifactId>
  <version>0.0.2</version>
</dependency>
```

**Gradle**

```groovy
dependencies {
    implementation 'io.github.realrains.kbrn:kbrn:0.0.2'
}
```

## Usage

### 사업자등록번호 문자열

**형식 및 체크섬 검증**

```java
KbrnUtils.isValid("1208147521");    // true
KbrnUtils.isValid("120-81-47521");  // true
KbrnUtils.isValid("120123");        // false, 형식 불일치
KbrnUtils.isValid("1208147520");    // false, 체크섬 불일치
KbrnUtils.isValid("120-81-47520");  // false, 체크섬 불일치
```

**형식 검증:**

```java
// 모든 사업자등록번호 형식 검증
KbrnUtils.isValidFormat("1208147521");   // true
KbrnUtils.isValidFormat("120-81-47521"); // true

// 기본 형식 검증
KbrnUtils.isValidPlainFormat("1208147521"); // true

// 구분 기호로 분리된 형식 검증
KbrnUtils.isValidDelimitedFormat("120-81-47521"); // true
```

**체크섬 계산 및 검증:**

```java
// 체크섬 계산
String body = "120814752";  // 앞 9자리
KbrnUtils.checksumOf(body); // '1'

// 체크섬 유효성 검증
KbrnUtils.hasValidChecksum("1208147521");   // true
KbrnUtils.hasValidChecksum("1208147520");   // false
KbrnUtils.hasValidChecksum("120-81-47521"); // true
KbrnUtils.hasValidChecksum("120-81-47520"); // false
```

**형식 변환:**

```java
KbrnUtils.toDelimitedFormat("1208147521"); // "120-81-47521"
KbrnUtils.toPlainFormat("120-81-47521");   // "1208147521"
```

### 사업자등록번호 객체 (KBRN)

**객체 생성:**

```java
KBRN kbrn1 = KBRN.valueOf("1208147521");
KBRN kbrn2 = KBRN.valueOf("120-81-47521");

kbrn1.equals(kbrn2); // true
```

**값 조회:**

```java
KBRN kbrn = KBRN.valueOf("120-81-47521");

kbrn.plainValue();      // "1208147521"
kbrn.delimitedValue();  // "120-81-47521"
```

**사업자등록번호 구성 요소 추출:**

```java
KBRN kbrn = KBRN.valueOf("120-81-47521");

kbrn.serialPrefix();      // "120" (일련번호, 앞 3자리)
kbrn.businessTypeCode();  // "81" (업종코드, 중간 2자리)
kbrn.serialSuffix();      // "47521" (일련번호, 마지막 5자리)

kbrn.body();              // "120814752" (체크섬 검사대상, 앞 9자리)
kbrn.checksum();          // '1' (체크섬, 마지막 1자리)
```

## 라이센스

이 프로젝트는 [LICENSE](/LICENSE) 파일에 명시된 라이센스에 따라 배포됩니다.
