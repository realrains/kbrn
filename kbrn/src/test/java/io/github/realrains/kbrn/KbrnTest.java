package io.github.realrains.kbrn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("KBRN 객체 테스트")
class KbrnTest {

    @Test
    @DisplayName("10자리 숫자로 이루어진 사업자등록번호 문자열로부터 KBRN 객체를 생성할 수 있다")
    void create_kbrn_from_ten_digit_string() {
        String value = "1208147521";
        KBRN kbrn = KBRN.valueOf(value);

        assertEquals(value, kbrn.value());
    }

    @Test
    @DisplayName("구분 기호가 있는 사업자등록번호 문자열로부터 KBRN 객체를 생성할 수 있다")
    void create_kbrn_from_delimited_ten_digit_string() {
        String value = "120-81-47521";
        KBRN kbrn = KBRN.valueOf(value);

        assertEquals("1208147521", kbrn.value());
    }

    @DisplayName("같은 사업자등록번호 문자열로부터 생성된 KBRN 객체는 동등하다")
    @Test
    void kbrn_equality_with_same_value() {
        String value = "120-81-47521";
        KBRN kbrn1 = KBRN.valueOf(value);
        KBRN kbrn2 = KBRN.valueOf(value);

        assertEquals(kbrn1, kbrn2);
    }

    @Test
    @DisplayName("형식이 달라도 같은 사업자등록번호 문자열로부터 생성된 KBRN 이라면 동등하다")
    void kbrn_equality_with_different_formats() {
        String value1 = "1208147521";
        String value2 = "120-81-47521";
        KBRN kbrn1 = KBRN.valueOf(value1);
        KBRN kbrn2 = KBRN.valueOf(value2);

        assertEquals(kbrn1, kbrn2);
    }

    @Test
    @DisplayName("다른 사업자등록번호 문자열로부터 생성된 KBRN 은 동등하지 않다")
    void kbrn_inequality_with_different_values() {
        String value1 = "120-81-47521";
        String value2 = "220-81-62517";
        KBRN kbrn1 = KBRN.valueOf(value1);
        KBRN kbrn2 = KBRN.valueOf(value2);

        assertNotEquals(kbrn1, kbrn2);
    }

    @DisplayName("올바르지 않은 형식의 사업자등록번호 문자열로 KBRN 객체를 생성할 때 예외가 발생한다")
    @ParameterizedTest(name = "값 = {0}")
    @NullAndEmptySource
    @ValueSource(strings = {
        "120814752", // 9자리 숫자
        "12081475212", // 11자리 숫자
        "120814752A", // 숫자가 아닌 문자 포함
        "120-81-4752", // 구분 기호가 있는 9자리
        "120-81-475212", // 구분 기호가 있는 11자리
        "120-81-4752A", // 구분 기호가 있는 숫자가 아닌 문자 포함
        "120-81-4752-", // 구분 기호가 있는 잘못된 형식
        "12-081-47521", // 구분 기호의 위치가 잘못됨
        "120_814_7521", // 잘못된 구분 기호 사용
    })
    void create_kbrn_from_invalid_string(String value) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.valueOf(value));
    }

    @DisplayName("체크섬이 유효하지 않은 사업자등록번호 문자열로 KBRN 객체를 생성할 때 예외가 발생한다")
    @ParameterizedTest(name = "값 = {0}")
    @ValueSource(strings = {
        "120-81-47520",
        "120-81-47522",
        "120-81-47523",
        "120-81-47524",
        "120-81-47525",
        "120-81-47526",
        "120-81-47527",
        "120-81-47528",
        "120-81-47529"
    })
    void create_kbrn_from_invalid_checksum(String value) {
        assertThrows(IllegalArgumentException.class, () -> KBRN.valueOf(value));
    }

    @Test
    @DisplayName("KBRN 객체가 올바른 구분 기호 형식의 문자열을 반환한다")
    void get_delimited_value() {
        KBRN kbrn = KBRN.valueOf("2208162517");

        assertEquals("220-81-62517", kbrn.delimitedValue());
    }

    @Test
    @DisplayName("KBRN 객체에서 일련번호 접두사 (앞 3자리) 를 올바르게 추출한다")
    void get_serial_prefix() {
        KBRN kbrn = KBRN.valueOf("220-81-62517");

        assertEquals("220", kbrn.serialPrefix());
    }

    @Test
    @DisplayName("KBRN 객체에서 사업자 유형 코드 (중간 2자리) 를 올바르게 추출한다")
    void get_business_type_code() {
        KBRN kbrn = KBRN.valueOf("220-81-62517");

        assertEquals("81", kbrn.businessTypeCode());
    }

    @Test
    @DisplayName("KBRN 객체에서 일련번호 접미사 (뒤 5자리) 를 올바르게 추출한다")
    void get_serial_suffix() {
        KBRN kbrn = KBRN.valueOf("220-81-62517");

        assertEquals("62517", kbrn.serialSuffix());
    }

    @Test
    @DisplayName("KBRN 객체에서 검증 번호를 제외한 본체 (앞 9자리) 를 올바르게 추출한다")
    void get_body() {
        KBRN kbrn = KBRN.valueOf("220-81-62517");

        assertEquals("220816251", kbrn.body());
    }

    @Test
    @DisplayName("KBRN 객체에서 검증 숫자 (마지막 1자리) 를 올바르게 추출한다")
    void get_checksum() {
        KBRN kbrn = KBRN.valueOf("220-81-62517");

        assertEquals('7', kbrn.checksum());
    }

    @Test
    @DisplayName("KBRN 객체는 정해진 형식의 문자열로 표현된다")
    void kbrn_to_string() {
        KBRN kbrn = KBRN.valueOf("220-81-62517");

        assertEquals("KBRN{'220-81-62517'}", kbrn.toString());
    }
}
