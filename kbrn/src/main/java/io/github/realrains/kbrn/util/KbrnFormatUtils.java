package io.github.realrains.kbrn.util;

import java.util.regex.Pattern;

public final class KbrnFormatUtils {

    public static final Pattern DEFAULT_KBRN_PATTERN = Pattern.compile("^\\d{10}$");
    public static final Pattern DELIMITED_KBRN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$");

    private KbrnFormatUtils() {}

    /**
     * 주어진 값이 지정된 패턴과 일치하는지 검증합니다.
     *
     * @param value   검증할 값
     * @param pattern 일치 여부를 검사할 정규식 패턴
     * @return 값이 패턴과 일치하면 true, 그렇지 않으면 false
     */
    private static boolean isValidFormat(String value, Pattern pattern) {
        return value != null && pattern.matcher(value).matches();
    }

    /**
     * 주어진 값이 기본 사업자등록번호 형식 (10자리 숫자) 을 만족하는지 검증합니다.
     *
     * @param value 검증할 값
     * @return 값이 기본 형식이면 true, 그렇지 않으면 false
     * @see KbrnFormatUtils#DEFAULT_KBRN_PATTERN
     */
    public static boolean isValidFormat(String value) {
        return isValidFormat(value, DEFAULT_KBRN_PATTERN);
    }

    /**
     * 주어진 값이 구분 기호가 있는 사업자등록번호 형식 (3-2-5 자리 숫자) 을 만족하는지 검증합니다.
     *
     * @param value 검증할 값
     * @return 값이 구분 기호가 있는 형식이면 true, 그렇지 않으면 false
     * @see KbrnFormatUtils#DELIMITED_KBRN_PATTERN
     */
    public static boolean isValidDelimitedFormat(String value) {
        return isValidFormat(value, DELIMITED_KBRN_PATTERN);
    }

    /**
     * 사업자등록번호 문자열을 기본 형식에서 구분 기호가 있는 형식으로 변환합니다.
     *
     * @param value 기본 형식의 사업자등록번호 문자열
     * @return 구분 기호가 있는 형식의 사업자등록번호 문자열
     * @throws IllegalArgumentException 주어진 값이 기본 형식이 아닌 경우
     * @see KbrnFormatUtils#DELIMITED_KBRN_PATTERN
     * @see KbrnFormatUtils#DEFAULT_KBRN_PATTERN
     */
    public static String toDelimitedFormat(String value) {
        if (!isValidFormat(value)) {
            throw new IllegalArgumentException("Only default format KBRN can be converted to delimited format: " + value);
        }
        return String.format("%s-%s-%s", value.substring(0, 3), value.substring(3, 5), value.substring(5));
    }

    /**
     * 사업자등록번호 문자열을 구분 기호가 있는 형식에서 기본 형식으로 변환합니다.
     *
     * @param value 구분 기호가 있는 형식의 사업자등록번호 문자열
     * @return 기본 형식의 사업자등록번호 문자열
     * @throws IllegalArgumentException 주어진 값이 구분 기호가 있는 형식이 아닌 경우
     * @see KbrnFormatUtils#DEFAULT_KBRN_PATTERN
     * @see KbrnFormatUtils#DELIMITED_KBRN_PATTERN
     */
    public static String toDefaultFormat(String value) {
        if (!isValidDelimitedFormat(value)) {
            throw new IllegalArgumentException("Only delimited format KBRN can be converted to default format: " + value);
        }
        return value.replace("-", "");
    }

}
