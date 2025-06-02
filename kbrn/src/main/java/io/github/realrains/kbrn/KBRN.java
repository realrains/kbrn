package io.github.realrains.kbrn;

import io.github.realrains.kbrn.util.ChecksumUtils;
import io.github.realrains.kbrn.util.KbrnFormatUtils;

import java.util.Objects;

import static io.github.realrains.kbrn.util.KbrnFormatUtils.isValidDelimitedFormat;
import static io.github.realrains.kbrn.util.KbrnFormatUtils.isValidFormat;

/**
 * 사업자등록번호 (KBRN) 클래스
 * <p>
 * 이 클래스는 사업자등록번호를 표현하는 값 객체입니다.
 * 기본 형식은 10자리 숫자 (예: "1208147521") 이며, 구분 기호가 있는 형식 (예: "120-81-47521") 으로부터 생성할 수도 있습니다.
 * 사업자등록번호의 유효성을 검사하고, 형식을 변환하며, 각 부분을 추출하는 기능을 제공합니다.
 * <p>
 * 사용 예시:
 * <pre>
 * {@code
 * KBRN kbrn = KBRN.from("1208147521");
 * KBRN kbrn2 = KBRN.from("120-81-47521");
 * println(kbrn.equals(kbrn2));        // true
 * println(kbrn.value());              // "1208147521"
 * println(kbrn.formattedValue());     // "120-81-47521"
 * println(kbrn.serialPrefix());       // "120"
 * println(kbrn.businessTypeCode());   // "81"
 * println(kbrn.serialSuffix());       // "47521"
 * println(kbrn.body());               // "120814752"
 * println(kbrn.checksum());           // '1'
 * println(kbrn.hasValidChecksum());   // true
 * }
 * </pre>
 *
 * @see ChecksumUtils
 * @see KbrnFormatUtils
 */
public class KBRN {

    private static final String DELIMITER = "-";
    private final String value;

    protected KBRN(String value) {
        if (!isValidFormat(value)) {
            throw new IllegalArgumentException("Value must be in default format (e.g., \"1234567890\") : " + value);
        }
        if (!ChecksumUtils.hasValidChecksum(value)) {
            throw new IllegalArgumentException("Value must have a valid checksum: " + value);
        }
        this.value = value;
    }

    /**
     * 기본 형식의 사업자등록번호로부터 KBRN 객체를 생성합니다.
     *
     * @param value 10자리 숫자로 구성된 사업자등록번호 문자열 (예: "1234567890" 또는 "123-45-67890")
     * @return KBRN 객체
     * @throws IllegalArgumentException 주어진 값이 유효한 형식이 아닌 경우
     */
    public static KBRN valueOf(String value) {
        if (isValidFormat(value) || isValidDelimitedFormat(value)) {
            return new KBRN(value.replace(DELIMITER, ""));
        }
        throw new IllegalArgumentException("Value must be in valid format (e.g., \"1234567890\" or \"123-45-67890\") : " + value);
    }

    /**
     * KBRN 객체를 문자열 형식으로 변환한 값을 반환합니다.
     *
     * @return 10자리 숫자로 구성된 (예: 1234567890) 사업자등록번호 문자열
     */
    public String value() {
        return value;
    }

    /**
     * KBRN 객체를 구분 기호가 있는 형식의 문자열로 변환한 값을 반환합니다.
     *
     * @return 구분 기호가 있는 형식 (예: "123-45-67890") 의 사업자등록번호 문자열
     */
    public String delimitedValue() {
        return String.join(DELIMITER, serialPrefix(), businessTypeCode(), serialSuffix());
    }

    /**
     * 사업자등록번호 앞 3자리 (접두사 일련번호) 를 반환합니다.
     *
     * @return 사업자등록번호 앞 3자리 문자열 (예: 1234567890 -> "123")
     */
    public String serialPrefix() {
        return value.substring(0, 3);
    }

    /**
     * 사업자등록번호 중간 2자리 (법인구분 코드) 를 반환합니다.
     *
     * @return 사업자등록번호 중간 2자리 문자열 (예: 1234567890 -> "45")
     */
    public String businessTypeCode() {
        return value.substring(3, 5);
    }

    /**
     * 사업자등록번호 뒤 5자리 (일련번호 + 검증번호) 를 반환합니다.
     *
     * @return 사업자등록번호 뒤 5자리 문자열 (예: 1234567890 -> "67890")
     */
    public String serialSuffix() {
        return value.substring(5, 10);
    }

    /**
     * 검증번호를 제외한 사업자등록번호의 앞 9자리를 반환합니다.
     *
     * @return 사업자등록번호 앞 9자리 문자열 (예: 1234567890 -> "123456789")
     */
    public String body() {
        return value.substring(0, 9);
    }

    /**
     * 사업자등록번호의 검증번호를 반환합니다.
     *
     * @return 검증번호 문자 (예: 1234567890 -> '0')
     */
    public char checksum() {
        return value.charAt(9);
    }

    /**
     * 사업자등록번호의 검증번호가 유효한지 검사합니다.
     *
     * @return 검증번호가 유효하면 true, 그렇지 않으면 false
     */
    public boolean hasValidChecksum() {
        return ChecksumUtils.hasValidChecksum(value);
    }

    /**
     * 사업자등록번호의 객체 문자열 표현을 반환합니다.
     * KBRN 을 문자열로 변환하는데 사용하지 않고, 디버깅이나 로깅시 사용합니다.
     *
     * @return KBRN 객체의 문자 표현 (예: "KBRN{'123-45-67890'}")
     * @see #value()
     * @see #delimitedValue()
     */
    @Override
    public String toString() {
        return String.format("KBRN{'%s'}", delimitedValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) { return false; }
        KBRN kbrn = (KBRN) o;
        return Objects.equals(value, kbrn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
