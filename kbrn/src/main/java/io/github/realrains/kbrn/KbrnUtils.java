package io.github.realrains.kbrn;

import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * 사업자등록번호 문자열을 검증하고 변환하는 유틸리티 클래스
 */
public class KbrnUtils {

    private static final Pattern PLAIN_KBRN_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern DELIMITED_KBRN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$");
    private static final char[] CHECKSUM_WEIGHTS = { 1, 3, 7, 1, 3, 7, 1, 3, 5 };

    private KbrnUtils() { throw new UnsupportedOperationException("Cannot be instantiated"); }

    /**
     * 주어진 값이 유효한 사업자등록번호인지 검증합니다.<br/>
     * 이 메서드는 기본 형식과 구분자로 분리된 형식을 모두 지원하며, 체크섬도 검증합니다.
     *
     * @param value 검증할 값
     * @return 값이 유효한 사업자등록번호를 만족하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public static boolean isValid(@Nullable String value) {
        return isValidFormat(value) && hasValidChecksum(value);
    }

    /**
     * 주어진 값이 유효한 사업자등록번호 형식인지 검증합니다.<br/>
     * 이 메서드는 기본 형식과 구분자로 분리된 형식을 모두 지원합니다.
     *
     * @param value 검증할 값
     * @return 값이 유효한 사업자등록번호 형식을 만족하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public static boolean isValidFormat(@Nullable String value) {
        return isValidPlainFormat(value) || isValidDelimitedFormat(value);
    }

    /**
     * 주어진 값이 기본 사업자등록번호 형식을 만족하는지 검증합니다. (예: {@code "1208147521"})
     *
     * @param value 검증할 값
     * @return 값이 기본 사업자등록번호 형식을 만족하면 {@code true}, 그렇지 않으면 {@code false}
     * @see #PLAIN_KBRN_PATTERN
     */
    public static boolean isValidPlainFormat(@Nullable String value) {
        if (value == null) return false;
        return PLAIN_KBRN_PATTERN.matcher(value).matches();
    }

    /**
     * 주어진 값이 구분자로 분리된 사업자등록번호 형식을 만족하는지 검증합니다. (예: {@code "120-81-47521"})
     *
     * @param value 검증할 값
     * @return 값이 구분자로 분리된 사업자등록번호 형식을 만족하면 {@code true}, 그렇지 않으면 {@code false}
     * @see #DELIMITED_KBRN_PATTERN
     */
    public static boolean isValidDelimitedFormat(@Nullable String value) {
        if (value == null) return false;
        return DELIMITED_KBRN_PATTERN.matcher(value).matches();
    }

    /**
     * 사업자등록번호 문자열을 기본 형식에서 구분자로 분리된 형식으로 변환합니다.<br/>
     * 이 메서드는 멱등하게 동작합니다.
     *
     * @param value 숫자로 구성된 10자리 형식의 사업자등록번호 문자열 (예: {@code "1208147521"})
     * @return 구분자로 구분된 형식의 사업자등록번호 문자열 (예: {@code "120-81-47521"})
     * @throws IllegalArgumentException {@code value} 가 숫자로 구성된 10자리 형식의 사업자등록번호 문자열이 아닌 경우
     * @see #DELIMITED_KBRN_PATTERN
     * @see #PLAIN_KBRN_PATTERN
     */
    public static String toDelimitedFormat(String value) {
        if (isValidDelimitedFormat(value)) {
            return value;
        }
        if (!isValidPlainFormat(value)) {
            throw new IllegalArgumentException("Cannot convert to delimited format: " + value);
        }
        return String.join("-", value.substring(0, 3), value.substring(3, 5), value.substring(5));
    }

    /**
     * 사업자등록번호 문자열을 구분자로 분리된 형식에서 기본 형식으로 변환합니다.<br/>
     * 이 메서드는 멱등하게 동작합니다.
     *
     * @param value 구분자로 구분된 형식의 사업자등록번호 문자열 (예: {@code "120-81-47521"})
     * @return 숫자로 구성된 10자리 형식의 사업자등록번호 문자열 (예: {@code "1208147521"})
     * @throws IllegalArgumentException {@code value} 가 올바른 사업자등록번호 문자열이 아닌 경우
     * @see #PLAIN_KBRN_PATTERN
     * @see #DELIMITED_KBRN_PATTERN
     */
    public static String toPlainFormat(String value) {
        if (isValidPlainFormat(value)) {
            return value;
        }
        if (!isValidDelimitedFormat(value)) {
            throw new IllegalArgumentException("Cannot convert to default format: " + value);
        }
        return value.replace("-", "");
    }

    /**
     * 사업자등록번호 앞 9자리 문자에 대한 체크섬을 계산합니다.
     *
     * @param body 숫자로 구성된 길이 9의 문자 시퀀스
     * @return 계산된 체크섬 문자
     * @throws IllegalArgumentException 입력이 유효한 길이 9의 문자 배열이 아니거나 숫자가 아닌 문자를 포함하는 경우.
     * @see KBRN#body()
     */
    public static char checksumOf(CharSequence body) {
        if (body.length() != 9) {
            throw new IllegalArgumentException("Body must be a character array of length 9");
        }

        int ws = 0;
        for (int i = 0; i < body.length(); i++) {
            if (!Character.isDigit(body.charAt(i))) {
                throw new IllegalArgumentException("All characters must be digits");
            }
            ws += (body.charAt(i) - '0') * CHECKSUM_WEIGHTS[i];
        }

        return (char) ('0' + ((10 - ((ws + (((body.charAt(8) - '0') * 5) / 10)) % 10)) % 10));
    }

    /**
     * 주어진 사업자등록번호 문자열이 유효한 체크섬을 가지고 있는지 확인합니다.
     *
     * @param value 사업자등록번호 문자열
     * @return 사업자등록번호가 유효한 체크섬을 포함하고 있으면 {@code true}, 그렇지 않으면 {@code false}.
     * @throws IllegalArgumentException 주어진 값이 유효한 형식이 아닌 경우.
     * @see #checksumOf(CharSequence)
     */
    public static boolean hasValidChecksum(String value) {
        String normalized = toPlainFormat(value);
        String body = normalized.substring(0, 9);
        char expected = normalized.charAt(9);
        return expected == checksumOf(body);
    }

}
