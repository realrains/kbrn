package io.github.realrains.kbrn.util;

public class ChecksumUtils {

    private static final char[] CHECKSUM_WEIGHTS = {1, 3, 7, 1, 3, 7, 1, 3, 5};

    private ChecksumUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 주어진 사업자등록번호 앞 9자리에 대한 마지막 체크섬 문자 [0-9] 를 계산합니다.
     *
     * @param body [0-9] 문자로 이루어진 char 배열
     * @return [0-9] 중 하나의 문자 체크섬
     * @throws IllegalArgumentException body 가 null 이거나 길이가 9가 아닐 경우 혹은 [0-9] 문자가 아닌 경우
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
     * 주어진 사업자등록번호 앞 9자리에 대한 마지막 체크섬 문자 [0-9] 를 계산합니다.
     *
     * @param body [0-9] 문자로 이루어진 문자열
     * @return [0-9] 중 하나의 문자 체크섬으로 이루어진 길이 1 의 String
     * @throws IllegalArgumentException value 가 null 이거나 길이가 9가 아닐 경우 혹은 [0-9] 문자가 아닌 경우
     */
    public static char checksum(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Input must be a string of length 9");
        }
        return checksum(body.toCharArray());
    }

}
