package io.github.realrains.kbrn;

import io.github.realrains.kbrn.test.ValidKbrnSource;
import io.github.realrains.kbrn.test.InvalidKbrnSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;

import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.CHECKSUM;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("KbrnUtils 테스트")
class KbrnUtilsTest {

    @DisplayName("주어진 문자열이 포맷과 체크섬이 올바른 사업자등록번호인지 검사")
    @ParameterizedTest(name = "CASE {index} - {0} / {1}")
    @ValidKbrnSource
    void check_valid_kbrn(String defaultValue, String delimitedValue) {
        assertTrue(KbrnUtils.isValid(defaultValue));
        assertTrue(KbrnUtils.isValid(delimitedValue));
    }

    @DisplayName("주어진 문자열이 포맷 또는 체크섬이 올바르지 않은 사업자등록번호인지 검사")
    @ParameterizedTest(name = "CASE {index} = {0} / {1}")
    @InvalidKbrnSource
    void check_invalid_kbrn(String defaultValue, String delimitedValue) {
        assertFalse(KbrnUtils.isValid(defaultValue));
        assertFalse(KbrnUtils.isValid(delimitedValue));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 형식인지 검사")
    @ParameterizedTest(name = "CASE {index} = {0} / {1}")
    @ValidKbrnSource
    void check_valid_kbrn_format(String defaultValue, String delimitedValue) {
        assertTrue(KbrnUtils.isValidFormat(defaultValue));
        assertTrue(KbrnUtils.isValidFormat(delimitedValue));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 체크섬을 가지는지 검사")
    @ParameterizedTest(name = "CASE {index} = {0} / {1}")
    @ValidKbrnSource
    void check_valid_kbrn_checksum(String defaultValue, String delimitedValue) {
        assertTrue(KbrnUtils.hasValidChecksum(defaultValue));
        assertTrue(KbrnUtils.hasValidChecksum(delimitedValue));
    }

    @DisplayName("주어진 문자열이 체크섬이 올바르지 않은 경우 검사")
    @ParameterizedTest(name = "CASE {index} = {0} / {1}")
    @InvalidKbrnSource(violations = CHECKSUM)
    void check_invalid_kbrn_checksum(String defaultValue, String delimitedValue) {
        assertFalse(KbrnUtils.hasValidChecksum(defaultValue));
        assertFalse(KbrnUtils.hasValidChecksum(delimitedValue));
    }

}
