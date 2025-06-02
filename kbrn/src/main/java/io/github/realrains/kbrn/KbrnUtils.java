package io.github.realrains.kbrn;

import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

public class KbrnUtils {

    private static final Pattern DEFAULT_KBRN_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern DELIMITED_KBRN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$");
    private static final char[] CHECKSUM_WEIGHTS = { 1, 3, 7, 1, 3, 7, 1, 3, 5 };

    private KbrnUtils() { }

    /**
     * 주어진 값이 지정된 패턴과 일치하는지 검증합니다.
     *
     * @param value   검증할 값
     * @param pattern 일치 여부를 검사할 정규식 패턴
     * @return 값이 패턴과 일치하면 true, 그렇지 않으면 false
     */
    private static boolean isValidFormat(@Nullable String value, Pattern pattern) {
        if (value == null) return false;
        return pattern.matcher(value).matches();
    }

    /**
     * 주어진 값이 기본 사업자등록번호 형식 (10자리 숫자) 을 만족하는지 검증합니다.
     *
     * @param value 검증할 값
     * @return 값이 기본 형식이면 true, 그렇지 않으면 false
     * @see #DEFAULT_KBRN_PATTERN
     */
    public static boolean isValidDefaultFormat(@Nullable String value) {
        return isValidFormat(value, DEFAULT_KBRN_PATTERN);
    }

    /**
     * 주어진 값이 구분 기호가 있는 사업자등록번호 형식 (3-2-5 자리 숫자) 을 만족하는지 검증합니다.
     *
     * @param value 검증할 값
     * @return 값이 구분 기호가 있는 형식이면 true, 그렇지 않으면 false
     * @see #DELIMITED_KBRN_PATTERN
     */
    public static boolean isValidDelimitedFormat(@Nullable String value) {
        return isValidFormat(value, DELIMITED_KBRN_PATTERN);
    }

    /**
     * 사업자등록번호 문자열을 기본 형식에서 구분 기호가 있는 형식으로 변환합니다.
     *
     * @param value 기본 형식의 사업자등록번호 문자열
     * @return 구분 기호가 있는 형식의 사업자등록번호 문자열
     * @throws IllegalArgumentException 주어진 값이 기본 형식이 아닌 경우
     * @see #DELIMITED_KBRN_PATTERN
     * @see #DEFAULT_KBRN_PATTERN
     */
    public static String toDelimitedFormat(String value) {
        if (!isValidDefaultFormat(value)) {
            throw new IllegalArgumentException("Only default format KBRN can be converted to delimited format: " + value);
        }
        return String.join("-", value.substring(0, 3), value.substring(3, 5), value.substring(5));
    }

    /**
     * 사업자등록번호 문자열을 구분 기호가 있는 형식에서 기본 형식으로 변환합니다.
     *
     * @param value 구분 기호가 있는 형식의 사업자등록번호 문자열
     * @return 기본 형식의 사업자등록번호 문자열
     * @throws IllegalArgumentException 주어진 값이 구분 기호가 있는 형식이 아닌 경우
     * @see #DEFAULT_KBRN_PATTERN
     * @see #DELIMITED_KBRN_PATTERN
     */
    public static String toDefaultFormat(String value) {
        if (!isValidDelimitedFormat(value)) {
            throw new IllegalArgumentException("Only delimited format KBRN can be converted to default format: " + value);
        }
        return value.replace("-", "");
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
        if (body.length != 9) {
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
        if (!isValidDefaultFormat(value)) {
            throw new IllegalArgumentException("Value must be in default format (e.g., \"1234567890\") : " + value);
        }

        char[] body = value.substring(0, 9).toCharArray();
        char expectedChecksum = value.charAt(9);
        char actualChecksum = checksum(body);
        return expectedChecksum == actualChecksum;
    }

}
