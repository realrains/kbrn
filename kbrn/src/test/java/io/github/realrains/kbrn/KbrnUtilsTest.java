package io.github.realrains.kbrn;

import io.github.realrains.kbrn.helper.ValidKbrnSource;
import io.github.realrains.kbrn.helper.InvalidKbrnSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.ADD;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.CHECKSUM;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.MOVE_HYPHEN;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.REMOVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("KbrnUtils 테스트")
class KbrnUtilsTest {

    @DisplayName("주어진 문자열이 포맷과 체크섬이 올바른 사업자등록번호인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(limit = 30)
    void check_valid_kbrn(String value) {
        assertTrue(KbrnUtils.isValid(value));
    }

    @DisplayName("주어진 문자열이 포맷 또는 체크섬이 올바르지 않은 사업자등록번호인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(checksumVariations = 2, limit = 100)
    void check_invalid_kbrn(String value) {
        assertFalse(KbrnUtils.isValid(value));
    }

    @DisplayName("주어진 문자열이 null 또는 빈 문자열인 경우 올바르지 않은 사업자등록번호라고 판단")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @NullAndEmptySource
    void check_null_or_empty(String value) {
        assertFalse(KbrnUtils.isValid(value));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 형식인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(limit = 30)
    void check_valid_kbrn_format(String value) {
        assertTrue(KbrnUtils.isValidFormat(value));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 형식이 아닌 경우 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(violations = { REMOVE, ADD, MOVE_HYPHEN }, limit = 50)
    void check_invalid_kbrn_format(String value) {
        assertFalse(KbrnUtils.isValidFormat(value));
    }

    @DisplayName("주어진 문자열이 null 또는 빈 문자열인 경우 올바른 사업자등록번호 형식이 아니라고 판단")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @NullAndEmptySource
    void check_null_or_empty_format(String value) {
        assertFalse(KbrnUtils.isValidFormat(value));
    }

    @DisplayName("주어진 문자열이 구분자를 포함한 올바른 사업자등록번호 형식인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(plain = false, delimited = true, limit = 20)
    void check_valid_delimited_kbrn_format(String value) {
        assertTrue(KbrnUtils.isValidDelimitedFormat(value));
    }

    @DisplayName("주어진 문자열이 구분자를 포함한 올바른 사업자등록번호 형식이 아닌 경우 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @NullAndEmptySource
    @ValidKbrnSource(plain = true, delimited = false, limit = 20)
    @InvalidKbrnSource(violations = { REMOVE, ADD, MOVE_HYPHEN }, limit = 30)
    void check_invalid_delimited_kbrn_format(String value) {
        assertFalse(KbrnUtils.isValidDelimitedFormat(value));
    }

    @DisplayName("주어진 문자열이 숫자만으로 구성된 10자리 사업자등록번호 형식인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(plain = true, delimited = false, limit = 20)
    void check_valid_plain_kbrn_format(String value) {
        assertTrue(KbrnUtils.isValidPlainFormat(value));
    }

    @DisplayName("주어진 문자열이 숫자만으로 구성된 10자리 사업자등록번호 형식이 아닌 경우 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(plain = false, delimited = true, limit = 20)
    @InvalidKbrnSource(violations = { REMOVE, ADD, MOVE_HYPHEN }, limit = 30)
    void check_invalid_plain_kbrn_format(String value) {
        assertFalse(KbrnUtils.isValidPlainFormat(value));
    }

    @DisplayName("주어진 문자열을 구분자로 분리된 사업자등록번호 형식으로 변환")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(plain = true, delimited = false, limit = 20)
    void convert_to_delimited_kbrn_format(String value) {
        String expected = value.substring(0, 3) + "-" + value.substring(3, 5) + "-" + value.substring(5);
        assertEquals(expected, KbrnUtils.toDelimitedFormat(value));
    }

    @DisplayName("주어진 문자열이 구분자로 분리된 사업자등록번호 형식인 경우 변환 시 동일한 값을 반환")
    @Test
    void convert_to_delimited_kbrn_format_already_delimited() {
        String value = "120-81-47521";
        assertEquals(value, KbrnUtils.toDelimitedFormat(value));
    }

    @DisplayName("주어진 문자열이 기본 형식의 사업자등록번호가 아닌 경우 변환 시 예외를 던짐")
    @Test
    void convert_to_delimited_kbrn_format_invalid() {
        String value = "120814752"; // 10자리 숫자
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.toDelimitedFormat(value));
    }

    @DisplayName("주어진 문자열을 구분자로 분리된 사업자등록번호 형식에서 기본 형식으로 변환")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(delimited = true, plain = false, limit = 20)
    void convert_to_plain_kbrn_format(String value) {
        String expected = value.replace("-", "");
        assertEquals(expected, KbrnUtils.toPlainFormat(value));
    }

    @DisplayName("주어진 문자열이 기본 형식의 사업자등록번호인 경우 변환 시 동일한 값을 반환")
    @Test
    void convert_to_plain_kbrn_format_already_plain() {
        String value = "1208147521";
        assertEquals(value, KbrnUtils.toPlainFormat(value));
    }

    @DisplayName("주어진 문자열이 구분자로 분리된 사업자등록번호 형식이 아닌 경우 변환 시 예외를 던짐")
    @Test
    void convert_to_plain_kbrn_format_invalid() {
        String value = "120-81-4752"; // 9자리 숫자
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.toPlainFormat(value));
    }

    @DisplayName("주어진 문자열에 대한 체크섬을 계산")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(delimited = false, limit = 20)
    void calculate_kbrn_checksum(String value) {
        String body = value.substring(0, 9);
        char checksum = value.charAt(9);
        assertEquals(checksum, KbrnUtils.checksumOf(body));
    }

    @DisplayName("9자리 숫자로 이루어진 문자가 아닌 경우 체크섬 계산 시 예외를 던짐")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValueSource(strings = {
        "",            // 빈 문자열
        "1208147521",  // 10자리 숫자
        "12081475A",   // 숫자가 아닌 문자 포함
        "12081475",    // 8자리 숫자
        "120-814-7521" // 구분자가 포함된 문자열
    })
    void calculate_invalid_kbrn_checksum(String value) {
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.checksumOf(value));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 체크섬을 가지는지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(limit = 30)
    void check_valid_kbrn_checksum(String value) {
        assertTrue(KbrnUtils.hasValidChecksum(value));
    }

    @DisplayName("주어진 문자열이 체크섬이 올바르지 않은 경우 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(violations = CHECKSUM, checksumVariations = 2, limit = 40)
    void check_invalid_kbrn_checksum(String value) {
        assertFalse(KbrnUtils.hasValidChecksum(value));
    }

    @DisplayName("체크섬 검사시 주어진 문자열이 올바른 사업자등록번호 형식이 아닌 경우 예외를 던짐")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(violations = { REMOVE, ADD, MOVE_HYPHEN }, limit = 30)
    void check_invalid_kbrn_checksum_from_invalid_format(String value) {
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.hasValidChecksum(value));
    }

    @DisplayName("체크섬 검사시 주어진 문자열이 null 또는 빈 문자열인 경우 예외를 던짐")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @NullAndEmptySource
    void check_null_or_empty_checksum(String value) {
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.hasValidChecksum(value));
    }
}
