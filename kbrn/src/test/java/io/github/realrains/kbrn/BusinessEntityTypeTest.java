package io.github.realrains.kbrn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.github.realrains.kbrn.BusinessEntityType.FOR_PROFIT_CORPORATE_BRANCH;
import static io.github.realrains.kbrn.BusinessEntityType.FOR_PROFIT_CORPORATE_HQ;
import static io.github.realrains.kbrn.BusinessEntityType.INDIVIDUAL_TAXABLE;
import static io.github.realrains.kbrn.BusinessEntityType.INDIVIDUAL_TAX_EXEMPT;
import static io.github.realrains.kbrn.BusinessEntityType.NON_PROFIT_CORPORATION;
import static io.github.realrains.kbrn.BusinessEntityType.UNDEFINED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("사업자 유형 코드 테스트")
class BusinessEntityTypeTest {

    @DisplayName("사업자 유형 코드가 올바르게 매핑되는지 확인")
    @Test
    void mapping_code_to_business_entity_type() {
        for (int i = 0; i < 100; i++) {
            String code = formatCode(i);
            BusinessEntityType type = BusinessEntityType.of(code);

            if (i >= 1 && i <= 79) {
                assertEquals(INDIVIDUAL_TAXABLE, type);
            } else if (i >= 90) {
                assertEquals(INDIVIDUAL_TAX_EXEMPT, type);
            } else if (i == 81 || i == 86 || i == 87 || i == 88) {
                assertEquals(FOR_PROFIT_CORPORATE_HQ, type);
            } else if (i == 82) {
                assertEquals(NON_PROFIT_CORPORATION, type);
            } else if (i == 85) {
                assertEquals(FOR_PROFIT_CORPORATE_BRANCH, type);
            } else {
                assertEquals(UNDEFINED, type);
            }
        }
    }

    @DisplayName("올바르지 않은 사업자 유형 코드에 대한 예외 처리")
    @ParameterizedTest
    @ValueSource(strings = {"", "ab", "01a"})
    void invalid_code_throws_exception(String code) {
        assertThrows(IllegalArgumentException.class, () -> BusinessEntityType.of(code));
    }

    private static String formatCode(int code) {
        return String.format("%02d", code);
    }

}
