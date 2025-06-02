package io.github.realrains.kbrn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KbrnTest {

    @Test
    @DisplayName("유효한 기본 형식의 문자열 (숫자 10자리) 로 KBRN 객체를 생성할 수 있다")
    void createKbrn() {
        String value = "2208162517";
        KBRN kbrn = KBRN.valueOf(value);
        assertEquals(value, kbrn.value());
    }

    @Test
    @DisplayName("기본 형식 문자열 (숫자 10자리) 가 아닌 문자열로 KBRN 생성 시 예외를 던진다")
    void createKbrnWithInvalidValue() {
        String value = "22081625170";
        assertThrows(IllegalArgumentException.class, () -> KBRN.valueOf(value));
    }

    @Test
    @DisplayName("KBRN 객체가 올바른 구분 기호 형식의 문자열을 반환한다")
    void getDelimitedValue() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertEquals("220-81-62517", kbrn.delimitedValue());
    }

    @Test
    @DisplayName("KBRN 객체에서 일련번호 접두사 (앞 3자리) 를 올바르게 추출한다")
    void getSerialPrefix() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertEquals("220", kbrn.serialPrefix());
    }

    @Test
    @DisplayName("KBRN 객체에서 사업자 유형 코드 (중간 2자리) 를 올바르게 추출한다")
    void getBusinessTypeCode() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertEquals("81", kbrn.businessTypeCode());
    }

    @Test
    @DisplayName("KBRN 객체에서 일련번호 접미사 (뒤 5자리) 를 올바르게 추출한다")
    void getSerialSuffix() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertEquals("62517", kbrn.serialSuffix());
    }

    @Test
    @DisplayName("KBRN 객체에서 검증 번호를 제외한 본체 (앞 9자리) 를 올바르게 추출한다")
    void getBody() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertEquals("220816251", kbrn.body());
    }

    @Test
    @DisplayName("KBRN 객체에서 검증 숫자 (마지막 1자리) 를 올바르게 추출한다")
    void getChecksum() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertEquals('7', kbrn.checksum());
    }

    @Test
    @DisplayName("유효한 검증 숫자를 가진 KBRN 객체는 체크섬 검증에 성공한다")
    void hasValidChecksum() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        assertTrue(kbrn.hasValidChecksum());
    }

    @Test
    @DisplayName("동일한 값을 가진 KBRN 객체는 서로 동등하고, 다른 값이나 타입과는 동등하지 않다")
    void testEquals() {
        KBRN kbrn1 = KBRN.valueOf("2208162517");
        KBRN kbrn2 = KBRN.valueOf("2208162517");
        KBRN kbrn3 = KBRN.valueOf("1208147521");

        assertEquals(kbrn1, kbrn2);
        assertEquals(kbrn2, kbrn1);
        assertNotEquals(kbrn1, kbrn3);
        assertNotEquals(null, kbrn1);
    }

    @Test
    @DisplayName("KBRN 객체는 정해진 형식의 문자열로 표현된다")
    void testToString() {
        KBRN kbrn = KBRN.valueOf("2208162517");
        System.out.println(UUID.randomUUID());
        assertEquals("KBRN{'220-81-62517'}", kbrn.toString());
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 길이의 KBRN 값으로 객체 생성 시 예외를 던진다")
    @ValueSource(strings = { "123456789", "12345678901", "", "123456789A" })
    void invalidLengthKbrnTest(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.valueOf(invalidValue));
    }

    @ParameterizedTest
    @DisplayName("Null 또는 빈 문자열로 KBRN 객체 생성 시 예외를 던진다")
    @NullAndEmptySource
    void nullOrEmptyKbrnTest(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.valueOf(invalidValue));
    }

}
