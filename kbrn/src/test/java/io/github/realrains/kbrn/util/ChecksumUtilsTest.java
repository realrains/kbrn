package io.github.realrains.kbrn.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChecksumUtilsTest {

    @DisplayName("주어진 사업자등록번호 앞 9자리로 체크섬 계산")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void calculate_checksum(String kbrn) {
        String body = kbrn.substring(0, 9);
        char expectedChecksum = kbrn.charAt(9);

        assertEquals(expectedChecksum, ChecksumUtils.checksum(body.toCharArray()));
        assertEquals(expectedChecksum, ChecksumUtils.checksum(body));
    }

    @DisplayName("체크섬 계산시 유효하지 않은 입력에 대한 예외 처리")
    @ParameterizedTest(name = "입력 : {0}")
    @NullAndEmptySource
    @ValueSource(strings = {
        "12345678",
        "12345678901",
        "12345678a",
        "123-22-12345",
    })
    void calculate_checksum_throws_exception_when_kbrn_is_invalid_format(String kbrn) {
        assertThrows(IllegalArgumentException.class, () -> ChecksumUtils.checksum(kbrn));
    }

    @DisplayName("주어진 사업자등록번호 문자열이 올바른 체크섬을 가지는지 검사")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void check_given_kbrn_has_valid_checksum(String kbrn) {
        assertTrue(ChecksumUtils.hasValidChecksum(kbrn));
    }

    @DisplayName("주어진 사업자등록번호 문자열이 올바른 체크섬을 가지지 않는지 검사")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @ValueSource(strings = {
        "1234567899",
        "0123456789",
        "9876543210"
    })
    void check_given_kbrn_has_invalid_checksum(String kbrn) {
        assertFalse(ChecksumUtils.hasValidChecksum(kbrn));
    }

    @DisplayName("주어진 사업자등록번호 문자열이 유효하지 않은 경우 체크섬 검사 예외 처리")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @NullAndEmptySource
    @ValueSource(strings = {
        "12345678",
        "12345678901",
        "12345678a",
        "123-22-12345",
    })
    void check_given_kbrn_throws_exception_when_kbrn_is_invalid_format(String kbrn) {
        assertThrows(IllegalArgumentException.class, () -> ChecksumUtils.hasValidChecksum(kbrn));
    }

}
