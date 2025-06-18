# KBRN Jackson 모듈

한국 사업자등록번호(KBRN)를 위한 Jackson 직렬화 지원 모듈입니다.

## 설치

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
    <version>0.0.3</version>
</dependency>
```

## Jackson 버전 호환성

이 모듈은 Jackson 2.12.0부터 2.19.x까지의 버전을 지원합니다:
- Jackson 2.12.x - Java 7 이상 필요
- Jackson 2.13.x ~ 2.19.x - Java 8 이상 필요

프로젝트에서 사용 중인 Jackson 버전이 이 범위 내에 있다면 해당 버전을 자동으로 사용합니다.

## 사용법

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.realrain.kbrn.jackson.KbrnModule;
import io.github.realrains.kbrn.KBRN;

// 모듈 등록
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new KbrnModule());

// 직렬화
KBRN kbrn = KBRN.valueOf("120-81-47521");
String json = mapper.writeValueAsString(kbrn); // "120-81-47521"

// 역직렬화
KBRN deserialized = mapper.readValue("\"1208147521\"", KBRN.class);
```

## 기능

- KBRN 객체를 구분자 형식 문자열로 직렬화 (예: "120-81-47521")
- 일반 형식(1208147521)과 구분자 형식(120-81-47521) 모두 역직렬화 지원
- null 값 적절히 처리
- 잘못된 KBRN 값에 대한 명확한 오류 메시지 제공
- 복잡한 데이터 구조 내의 KBRN 객체 지원

## DTO(Data Transfer Object) 사용 예제

```java
public class Company {
    private String name;
    private KBRN businessNumber;
    
    // getter와 setter
}

// 직렬화
Company company = new Company();
company.setName("예제 회사");
company.setBusinessNumber(KBRN.valueOf("120-81-47521"));

String json = mapper.writeValueAsString(company);
// {"name":"예제 회사","businessNumber":"120-81-47521"}

// 역직렬화
Company deserialized = mapper.readValue(json, Company.class);
```

## Spring Boot 통합

Spring Boot에서 사용하는 경우, `@Configuration` 클래스에서 자동으로 등록할 수 있습니다:

```java
@Configuration
public class JacksonConfig {
    
    @Bean
    public Module kbrnModule() {
        return new KbrnModule();
    }
}
```

## 라이선스

MIT License
