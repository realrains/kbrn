package io.github.realrains.kbrn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KbrnTest {

    @Test
    @DisplayName("유효한 기본 형식의 문자열 (숫자 10자리) 로 KBRN 객체를 생성할 수 있다")
    void createKbrn() {
        String value = "2208162517";
        KBRN kbrn = KBRN.from(value);
        assertEquals(value, kbrn.value());
    }

    @Test
    @DisplayName("기본 형식 문자열 (숫자 10자리) 가 아닌 문자열로 KBRN 생성 시 예외를 던진다")
    void createKbrnWithInvalidValue() {
        String value = "22081625170";
        assertThrows(IllegalArgumentException.class, () -> KBRN.from(value));
    }

    @Test
    @DisplayName("구분 기호가 포함된 문자열 (숫자 3-2-5 형식) 로 KBRN 객체를 생성할 수 있다")
    void createKbrnFromDelimited() {
        String delimitedValue = "220-81-62517";
        KBRN kbrn = KBRN.fromDelimited(delimitedValue);
        assertEquals("2208162517", kbrn.value());
    }

    @Test
    @DisplayName("유효하지 않은 구분 기호 형식의 문자열 (숫자 3-2-5 형식) 로 KBRN 생성 시 예외를 던진다")
    void createKbrnFromInvalidDelimited() {
        String delimitedValue = "220-81-625170";
        assertThrows(IllegalArgumentException.class, () -> KBRN.fromDelimited(delimitedValue));
    }

    @Test
    @DisplayName("KBRN 객체가 올바른 구분 기호 형식의 문자열을 반환한다")
    void getDelimitedValue() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals("220-81-62517", kbrn.delimitedValue());
    }

    @Test
    @DisplayName("KBRN 객체에서 일련번호 접두사 (앞 3자리) 를 올바르게 추출한다")
    void getSerialPrefix() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals("220", kbrn.serialPrefix());
    }

    @Test
    @DisplayName("KBRN 객체에서 사업자 유형 코드 (중간 2자리) 를 올바르게 추출한다")
    void getBusinessTypeCode() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals("81", kbrn.businessTypeCode());
    }

    @Test
    @DisplayName("KBRN 객체에서 일련번호 접미사 (뒤 5자리) 를 올바르게 추출한다")
    void getSerialSuffix() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals("62517", kbrn.serialSuffix());
    }

    @Test
    @DisplayName("KBRN 객체에서 검증 번호를 제외한 본체 (앞 9자리) 를 올바르게 추출한다")
    void getBody() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals("220816251", kbrn.body());
    }

    @Test
    @DisplayName("KBRN 객체에서 검증 숫자 (마지막 1자리) 를 올바르게 추출한다")
    void getChecksum() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals('7', kbrn.checksum());
    }

    @Test
    @DisplayName("유효한 검증 숫자를 가진 KBRN 객체는 체크섬 검증에 성공한다")
    void hasValidChecksum() {
        KBRN kbrn = KBRN.from("2208162517");
        assertTrue(kbrn.hasValidChecksum());
    }

    @Test
    @DisplayName("동일한 값을 가진 KBRN 객체는 서로 동등하고, 다른 값이나 타입과는 동등하지 않다")
    void testEquals() {
        KBRN kbrn1 = KBRN.from("2208162517");
        KBRN kbrn2 = KBRN.from("2208162517");
        KBRN kbrn3 = KBRN.from("1208147521");

        assertEquals(kbrn1, kbrn2);
        assertEquals(kbrn2, kbrn1);
        assertNotEquals(kbrn1, kbrn3);
        assertNotEquals(null, kbrn1);
    }

    @Test
    @DisplayName("KBRN 객체는 정해진 형식의 문자열로 표현된다")
    void testToString() {
        KBRN kbrn = KBRN.from("2208162517");
        assertEquals("KBRN{value='2208162517'}", kbrn.toString());
    }

    @ParameterizedTest
    @DisplayName("형식이 다른 동일한 사업자등록번호는 동일한 KBRN 객체로 생성된다")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void parameterizedValidKbrnTest(String kbrnValue, String kbrnDelimited) {
        KBRN kbrn1 = KBRN.from(kbrnValue);
        KBRN kbrn2 = KBRN.fromDelimited(kbrnDelimited);

        assertEquals(kbrnValue, kbrn1.value());
        assertEquals(kbrnValue, kbrn2.value());
        assertEquals(kbrnDelimited, kbrn1.delimitedValue());
        assertEquals(kbrnDelimited, kbrn2.delimitedValue());
        assertEquals(kbrn1, kbrn2);
    }

    @ParameterizedTest
    @DisplayName("올바른 체크섬을 가진 KBRN은 검증에 성공한다")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void validChecksumTest(String kbrnValue, String kbrnDelimited) {
        KBRN kbrn1 = KBRN.from(kbrnValue);
        KBRN kbrn2 = KBRN.fromDelimited(kbrnDelimited);

        assertTrue(kbrn1.hasValidChecksum());
        assertTrue(kbrn2.hasValidChecksum());
    }

    @ParameterizedTest
    @DisplayName("잘못된 체크섬을 가진 KBRN은 검증에 실패한다")
    @CsvSource({
        "2208162510, 220-81-62510",
        "1208147522, 120-81-47522",
        "1248100999, 124-81-00999"
    })
    void invalidChecksumTest(String kbrnValue, String kbrnDelimited) {
        KBRN kbrn1 = KBRN.from(kbrnValue);
        KBRN kbrn2 = KBRN.fromDelimited(kbrnDelimited);

        assertFalse(kbrn1.hasValidChecksum());
        assertFalse(kbrn2.hasValidChecksum());
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 길이의 KBRN 값으로 객체 생성 시 예외를 던진다")
    @ValueSource(strings = { "123456789", "12345678901", "", "123456789A" })
    void invalidLengthKbrnTest(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.from(invalidValue));
    }

    @ParameterizedTest
    @DisplayName("Null 또는 빈 문자열로 KBRN 객체 생성 시 예외를 던진다")
    @NullAndEmptySource
    void nullOrEmptyKbrnTest(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.from(invalidValue));
    }

    @ParameterizedTest
    @DisplayName("올바르지 않은 구분 기호 형식으로 KBRN 객체 생성 시 예외를 던진다")
    @ValueSource(strings = {
        "220-8162517", // 잘못된 구분 기호 위치
        "220812-6517", // 잘못된 구분 기호 위치
        "220/81/62517", // 잘못된 구분 기호 문자
        "220-81-6251-7", // 초과 구분 기호
        "220 81 62517", // 공백 구분 기호
        "22081-62517" // 누락된 구분 기호
    })
    void invalidDelimitedFormatTest(String invalidDelimitedValue) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.fromDelimited(invalidDelimitedValue));
    }
}
