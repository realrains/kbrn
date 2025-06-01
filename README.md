# KBRN (Korean Business Registration Number)

KBRN은 한국 사업자등록번호를 처리하기 위한 Java 라이브러리입니다.

## 주요 기능

- 사업자등록번호 유효성 검증
- 형식 변환 (기본 형식 ↔ 구분 기호 형식)
- 체크섬 검증
- 사업자등록번호 구성 요소 추출

## 설치 방법

### Gradle

```gradle
dependencies {
    implementation 'io.github.realrains:kbrn:x.y.z'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.realrains</groupId>
    <artifactId>kbrn</artifactId>
    <version>x.y.z</version>
</dependency>
```

## 사용 예시

이 라이브러리는 두 가지 방식으로 사용할 수 있습니다:
1. **문자열 기반 유틸리티 함수** - `KBRN` 객체 없이 사업자등록번호 문자열만으로 처리
2. **KBRN 객체 기반 메소드** - 사업자등록번호를 객체로 관리하여 다양한 기능 활용

### 1. 문자열 기반 사용법

문자열 기반으로 유틸리티 클래스를 통해 사업자등록번호 관련 작업을 처리할 수 있습니다.

#### 형식 검증

```java
// 기본 형식(10자리 숫자) 검증
boolean isValid = KbrnFormatUtils.isValidFormat("1208147521"); // true

// 구분 기호 형식(3-2-5 자리 숫자) 검증
boolean isDelimitedValid = KbrnFormatUtils.isValidDelimitedFormat("120-81-47521"); // true
```

#### 형식 변환

```java
// 기본 형식 → 구분 기호 형식으로 변환
String delimited = KbrnFormatUtils.toDelimitedFormat("1208147521"); // "120-81-47521"

// 구분 기호 형식 → 기본 형식으로 변환
String plain = KbrnFormatUtils.toDefaultFormat("120-81-47521"); // "1208147521"
```

#### 체크섬 검증

```java
// 체크섬 계산
String body = "120814752"; // 앞 9자리
char checksum = ChecksumUtils.checksum(body); // '1'

// 체크섬 유효성 검증
boolean isValid = ChecksumValidator.isValid("1208147521"); // true
```

### 2. KBRN 객체 기반 사용법

더 풍부한 기능과 타입 안전성을 위해 `KBRN` 객체를 활용할 수 있습니다.

#### 객체 생성

```java
// 기본 형식의 사업자등록번호로 객체 생성
KBRN kbrn1 = KBRN.from("1208147521");

// 구분 기호가 있는 형식으로 객체 생성
KBRN kbrn2 = KBRN.fromDelimited("120-81-47521");

// 두 객체는 동일합니다
boolean isEqual = kbrn1.equals(kbrn2); // true
```

#### 값 조회

```java
KBRN kbrn = KBRN.from("1208147521");

// 기본 형식 값 조회
String value = kbrn.value(); // "1208147521"

// 구분 기호 형식 값 조회
String delimitedValue = kbrn.delimitedValue(); // "120-81-47521"
```

#### 사업자등록번호 구성 요소 추출

```java
KBRN kbrn = KBRN.from("1208147521");

// 지역 코드 (앞 3자리)
String serialPrefix = kbrn.serialPrefix(); // "120"

// 업종 코드 (중간 2자리)
String businessTypeCode = kbrn.businessTypeCode(); // "81"

// 일련번호 (뒤 5자리)
String serialSuffix = kbrn.serialSuffix(); // "47521"

// 체크섬 검사 대상 (앞 9자리)
String body = kbrn.body(); // "120814752"

// 체크섬 값 (마지막 1자리)
char checksum = kbrn.checksum(); // '1'
```

#### 유효성 검증

```java
KBRN kbrn = KBRN.from("1208147521");

// 체크섬 유효성 검증
boolean isValid = kbrn.hasValidChecksum(); // true
```

## 라이센스

이 프로젝트는 LICENSE 파일에 명시된 라이센스에 따라 배포됩니다.

