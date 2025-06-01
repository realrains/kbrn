package io.github.realrains.kbrn.util;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChecksumUtilsTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/kbrn_sample.txt", useHeadersInDisplayName = true)
    void test_kbrn_checksum(String kbrn) {
        String body = kbrn.substring(0, 9);
        char expectedChecksum = kbrn.charAt(9);

        assertEquals(expectedChecksum, ChecksumUtils.checksum(body.toCharArray()));
        assertEquals(expectedChecksum, ChecksumUtils.checksum(body));
    }
}