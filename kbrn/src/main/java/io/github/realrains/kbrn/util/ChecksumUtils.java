package io.github.realrains.kbrn.util;

import io.github.realrains.kbrn.KBRN;

import static io.github.realrains.kbrn.util.KbrnFormatUtils.isValidFormat;

public final class ChecksumUtils {

    private static final char[] CHECKSUM_WEIGHTS = { 1, 3, 7, 1, 3, 7, 1, 3, 5 };

    private ChecksumUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 사업자등록번호 앞 9자리 문자 (body) 에 대한 체크섬을 계산합니다.
     *
     * @param body 숫자로 구성된 길이 9의 문자 배열.
     * @return 계산된 체크섬 문자.
     * @throws IllegalArgumentException 입력이 유효한 길이 9의 문자 배열이 아니거나 숫자가 아닌 문자를 포함하는 경우.
     * @see KBRN#body()
     */
    public static char checksum(char[] body) {
        if (body == null || body.length != 9) {
            throw new IllegalArgumentException("Input must be a character array of length 9");
        }

        int ws = 0;
        for (int i = 0; i < body.length; i++) {
            if (!Character.isDigit(body[i])) {
                throw new IllegalArgumentException("All characters must be digits");
            }
            ws += (body[i] - '0') * CHECKSUM_WEIGHTS[i];
        }

        return (char) ('0' + ((10 - ((ws + (((body[8] - '0') * 5) / 10)) % 10)) % 10));
    }

    /**
     * 사업자등록번호 앞 9자리 문자 (body) 에 대한 체크섬을 계산합니다.
     *
     * @param body 숫자로 구성된 길이 9의 문자열.
     * @return 계산된 체크섬 문자.
     * @throws IllegalArgumentException 입력이 유효한 길이 9의 문자열이 아니거나 숫자가 아닌 문자를 포함하는 경우.
     * @see KBRN#body()
     */
    public static char checksum(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Input must be a string of length 9");
        }
        return checksum(body.toCharArray());
    }

    /**
     * 주어진 사업자등록번호 문자열이 유효한 체크섬을 가지고 있는지 확인합니다.
     *
     * @param value 10자리 숫자로 구성된 사업자등록번호 문자열.
     * @return 유효한 체크섬이면 true, 그렇지 않으면 false.
     * @throws IllegalArgumentException 주어진 값이 유효한 형식이 아닌 경우.
     */
    public static boolean hasValidChecksum(String value) {
        if (!isValidFormat(value)) {
            throw new IllegalArgumentException("Value must be in default format (e.g., \"1234567890\") : " + value);
        }

        char[] body = value.substring(0, 9).toCharArray();
        char expectedChecksum = value.charAt(9);
        char actualChecksum = checksum(body);
        return expectedChecksum == actualChecksum;
    }
}
