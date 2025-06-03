package io.github.realrains.kbrn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("KbrnUtils 테스트")
class KbrnUtilsTest {

    @DisplayName("주어진 문자열이 올바른 사업자등록번호인지 검사")
    @ParameterizedTest(name = "값 = {0} / {1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void check_valid_kbrn(String defaultValue, String delimitedValue) {
        assertTrue(KbrnUtils.isValid(defaultValue));
        assertTrue(KbrnUtils.isValid(delimitedValue));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 형식인지 검사")
    @ParameterizedTest(name = "값 = {0} / {1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void check_valid_kbrn_format(String defaultValue, String delimitedValue) {
        assertTrue(KbrnUtils.isValidFormat(defaultValue));
        assertTrue(KbrnUtils.isValidFormat(delimitedValue));
    }

    @DisplayName("주어진 문자열이 올바른 사업자등록번호 체크섬을 가지는지 검사")
    @ParameterizedTest(name = "값 = {0} / {1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
    void check_valid_kbrn_checksum(String defaultValue, String delimitedValue) {
        assertTrue(KbrnUtils.hasValidChecksum(defaultValue));
        assertTrue(KbrnUtils.hasValidChecksum(delimitedValue));
    }

}
