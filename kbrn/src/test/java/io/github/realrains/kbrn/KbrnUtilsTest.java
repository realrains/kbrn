package io.github.realrains.kbrn;

import io.github.realrains.kbrn.test.ValidKbrnSource;
import io.github.realrains.kbrn.test.InvalidKbrnSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.ADD;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.CHECKSUM;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.MOVE_HYPHEN;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.REMOVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("KbrnUtils 테스트")
class KbrnUtilsTest {

    @DisplayName("주어진 문자열이 포맷과 체크섬이 올바른 사업자등록번호인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource
    void check_valid_kbrn(String value) {
        assertTrue(KbrnUtils.isValid(value));
    }

    @DisplayName("주어진 문자열이 포맷 또는 체크섬이 올바르지 않은 사업자등록번호인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource
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
    @ValidKbrnSource
    void check_valid_kbrn_format(String value) {
        assertTrue(KbrnUtils.isValidFormat(value));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 형식이 아닌 경우 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(violations = { REMOVE, ADD, MOVE_HYPHEN })
    void check_invalid_kbrn_format(String value) {
        assertFalse(KbrnUtils.isValidFormat(value));
    }

    @DisplayName("주어진 문자열이 null 또는 빈 문자열인 경우 올바른 사업자등록번호 형식이 아니라고 판단")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @NullAndEmptySource
    void check_null_or_empty_format(String value) {
        assertFalse(KbrnUtils.isValidFormat(value));
    }

    @DisplayName("주어진 문자열에 대한 체크섬을 계산")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @ValidKbrnSource(delimited = false)
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
    @ValidKbrnSource
    void check_valid_kbrn_checksum(String value) {
        assertTrue(KbrnUtils.hasValidChecksum(value));
    }

    @DisplayName("주어진 문자열이 체크섬이 올바르지 않은 경우 검사")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(violations = CHECKSUM)
    void check_invalid_kbrn_checksum(String value) {
        assertFalse(KbrnUtils.hasValidChecksum(value));
    }

    @DisplayName("체크섬 검사시 주어진 문자열이 올바른 사업자등록번호 형식이 아닌 경우 예외를 던짐")
    @ParameterizedTest(name = "CASE {index} - {0}")
    @InvalidKbrnSource(violations = { REMOVE, ADD, MOVE_HYPHEN })
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
