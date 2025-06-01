package io.github.realrains.kbrn.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KbrnFormatUtilsTest {

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void checkValidDefaultFormat(String value) {
        assertTrue(KbrnFormatUtils.isValidFormat(value));
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
        assertFalse(KbrnFormatUtils.isValidFormat(value));
    }

    @ParameterizedTest(name = "{1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void checkValidDeliminatedFormat(String kbrn, String delimitedKbrn) {
        assertTrue(KbrnFormatUtils.isValidDelimitedFormat(delimitedKbrn));
    }

    @ParameterizedTest
    @NullSource
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
        assertFalse(KbrnFormatUtils.isValidDelimitedFormat(value));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void fromDefaultToDelimited(String kbrn, String delimitedKbrn) {
        assertEquals(KbrnFormatUtils.toDelimitedFormat(kbrn), delimitedKbrn);
    }

    @ParameterizedTest(name = "{1} -> {0}")
    @CsvFileSource(resources = "/kbrn_sample.csv", useHeadersInDisplayName = true)
    void fromDelimitedToDefaultFormat(String kbrn, String delimitedKbrn) {
        assertEquals(KbrnFormatUtils.toDefaultFormat(delimitedKbrn), kbrn);
    }
}
