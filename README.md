# kbrn

**kbrn** 은 대한민국의 사업자등록번호 (Korean Business Registration Number) 를 처리하기 위한 Java 라이브러리입니다.
사업자등록번호의 형식 검증, 체크섬 계산을 통한 유효성 검사, 형식 변환 등을 간편하게 처리할 수 있도록 도와줍니다.

## Requirements
- JDK 11 이상

## Usage

### 1. 사업자등록번호 문자열

**형식 검증:**

```java
// 기본 형식(10자리 숫자) 검증
boolean isValid = KbrnFormatUtils.isValidFormat("1208147521"); // true

// 구분 기호 형식(3-2-5 자리 숫자) 검증
boolean isDelimitedValid = KbrnFormatUtils.isValidDelimitedFormat("120-81-47521"); // true
```

**형식 변환:**

```java
// 기본 형식 → 구분 기호 형식으로 변환
String delimited = KbrnFormatUtils.toDelimitedFormat("1208147521"); // "120-81-47521"

// 구분 기호 형식 → 기본 형식으로 변환
String plain = KbrnFormatUtils.toDefaultFormat("120-81-47521"); // "1208147521"
```

**체크섬 계산 및 검증:**

```java
// 체크섬 계산
String body = "120814752"; // 앞 9자리
ChecksumUtils.checksum(body); // '1'

// 체크섬 유효성 검증
ChecksumUtils.hasValidChecksum("1208147521"); // true
```

### 2. 사업자등록번호 객체 (KBRN)

더 풍부한 기능과 타입 안전성을 위해 `KBRN` 객체를 활용할 수 있습니다.

**객체 생성:**

```java
// 기본 형식의 사업자등록번호로 객체 생성
KBRN kbrn1 = KBRN.valueOf("1208147521");

// 구분 기호가 있는 형식으로 객체 생성
KBRN kbrn2 = KBRN.valueOf("120-81-47521");

// 두 객체는 동일합니다
kbrn1.equals(kbrn2); // true
```

**값 조회:**

```java
KBRN kbrn = KBRN.valueOf("120-81-47521");

kbrn.value(); // "1208147521"
kbrn.delimitedValue(); // "120-81-47521"
```

**사업자등록번호 구성 요소 추출:**

```java
KBRN kbrn = KBRN.from("120-81-47521");

// 지역 코드 (앞 3자리)
kbrn.serialPrefix(); // "120"

// 업종 코드 (중간 2자리)
kbrn.businessTypeCode(); // "81"

// 일련번호 (뒤 5자리)
kbrn.serialSuffix(); // "47521"

// 체크섬 검사 대상 (앞 9자리)
kbrn.body(); // "120814752"

// 체크섬 값 (마지막 1자리)
kbrn.checksum(); // '1'
```

## 라이센스

이 프로젝트는 LICENSE 파일에 명시된 라이센스에 따라 배포됩니다.

