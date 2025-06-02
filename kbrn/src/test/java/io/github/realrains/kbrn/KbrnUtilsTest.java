package io.github.realrains.kbrn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class KbrnUtilsTest {
    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void checkValidDefaultFormat(String value) {
        assertTrue(KbrnUtils.isValidDefaultFormat(value));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
        "",
        "012345678",
        "01234567890",
        "012345678a",
        "abcdefghij",
        "012-34-56789",
    })
    void checkInvalidDefaultFormat(String value) {
        assertFalse(KbrnUtils.isValidDefaultFormat(value));
    }

    @ParameterizedTest(name = "{1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void checkValidDeliminatedFormat(String kbrn, String delimitedKbrn) {
        assertTrue(KbrnUtils.isValidDelimitedFormat(delimitedKbrn));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "--",
        "012-34-5678",
        "01-234-56789",
        "0-1234-56789",
        "012-34-567890",
        "012-34-5678a",
        "abc-de-fghij",
        "0123456789",
    })
    void checkInvalidDelimitedFormat(String value) {
        assertFalse(KbrnUtils.isValidDelimitedFormat(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void fromDefaultToDelimited(String kbrn, String delimitedKbrn) {
        assertEquals(KbrnUtils.toDelimitedFormat(kbrn), delimitedKbrn);
    }

    @ParameterizedTest(name = "{1} -> {0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void fromDelimitedToDefaultFormat(String kbrn, String delimitedKbrn) {
        assertEquals(KbrnUtils.toDefaultFormat(delimitedKbrn), kbrn);
    }

    @DisplayName("주어진 사업자등록번호 앞 9자리로 체크섬 계산")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void calculate_checksum(String kbrn) {
        String body = kbrn.substring(0, 9);
        char expectedChecksum = kbrn.charAt(9);

        assertEquals(expectedChecksum, KbrnUtils.checksum(body.toCharArray()));
        assertEquals(expectedChecksum, KbrnUtils.checksum(body));
    }

    @DisplayName("체크섬 계산시 유효하지 않은 입력에 대한 예외 처리")
    @ParameterizedTest(name = "입력 : {0}")
    @ValueSource(strings = {
        "",
        "12345678",
        "12345678901",
        "12345678a",
        "123-22-12345",
    })
    void calculate_checksum_throws_exception_when_kbrn_is_invalid_format(String kbrn) {
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.checksum(kbrn));
    }

    @Test
    @DisplayName("체크섬 계산시 null 입력시 NPE 처리")
    void calculate_checksum_throws_exception_when_kbrn_is_null() {
        assertThrows(NullPointerException.class, () -> KbrnUtils.checksum((String) null));
        assertThrows(NullPointerException.class, () -> KbrnUtils.checksum((char[]) null));
    }

    @DisplayName("주어진 사업자등록번호 문자열이 올바른 체크섬을 가지는지 검사")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void check_given_kbrn_has_valid_checksum(String kbrn) {
        assertTrue(KbrnUtils.hasValidChecksum(kbrn));
    }

    @DisplayName("주어진 사업자등록번호 문자열이 올바른 체크섬을 가지지 않는지 검사")
    @ParameterizedTest(name = "사업자등록번호 : {0}")
    @ValueSource(strings = {
        "1234567899",
        "0123456789",
        "9876543210"
    })
    void check_given_kbrn_has_invalid_checksum(String kbrn) {
        assertFalse(KbrnUtils.hasValidChecksum(kbrn));
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
        assertThrows(IllegalArgumentException.class, () -> KbrnUtils.hasValidChecksum(kbrn));
    }
}
