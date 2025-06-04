package io.github.realrains.kbrn;

/**
 * 사업자등록번호의 중앙 2자리 숫자가 의미하는 사업자 유형을 나타내는 열거형입니다.
 *
 * @see <a href="https://call.nts.go.kr/call/qna/selectQnaInfo.do?mi=1329">국세상담센터 > 자주묻는Q&A</a>
 */
public enum BusinessEntityType {
    /**
     * 개인과세사업자: 01-79
     */
    INDIVIDUAL_TAXABLE,
    /**
     * 개인면세사업자: 90-99
     */
    INDIVIDUAL_TAX_EXEMPT,
    /**
     * 영리법인의 본점: 81, 86, 87, 88
     */
    FOR_PROFIT_CORPORATE_HQ,
    /**
     * 비영리법인: 82
     */
    NON_PROFIT_CORPORATION,
    /**
     * 영리법인의 지점: 85
     */
    FOR_PROFIT_CORPORATE_BRANCH,
    /**
     * 정의되지 않은 사업자 유형
     */
    UNDEFINED;

    /**
     * 주어진 문자열 값을 기반으로 BusinessEntityType 을 반환합니다.
     *
     * @param value 사업자 유형 코드 (예: "01", "82", "85" 등)
     * @return 해당하는 BusinessEntityType
     * @throws IllegalArgumentException 주어진 값이 유효하지 않은 경우
     */
    public static BusinessEntityType of(String value) {
        try {
            int code = Integer.parseInt(value);
            if (code >= 1 && code <= 79) {
                return INDIVIDUAL_TAXABLE;
            }
            if (code >= 90 && code <= 99) {
                return INDIVIDUAL_TAX_EXEMPT;
            }
            if (code == 81 || code == 86 || code == 87 || code == 88) {
                return FOR_PROFIT_CORPORATE_HQ;
            }
            if (code == 82) {
                return NON_PROFIT_CORPORATION;
            }
            if (code == 85) {
                return FOR_PROFIT_CORPORATE_BRANCH;
            }
            return UNDEFINED;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot convert to BusinessType: " + value);
        }
    }
}
