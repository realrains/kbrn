package io.github.realrains.kbrn.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.ADD;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.CHECKSUM;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.MOVE_HYPHEN;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.REMOVE;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(InvalidKbrnArgumentProvider.class)
public @interface InvalidKbrnSource {

    enum Strategy {
        CHECKSUM,
        REMOVE,
        ADD,
        MOVE_HYPHEN
    }

    /**
     * 기본 형식의 사업자등록번호를 포함할지 여부
     * @return {@code true} 이면 기본 형식의 사업자등록번호를 포함
     */
    boolean plain() default true;

    /**
     * 구분자로 분리된 형식의 사업자등록번호를 포함할지 여부
     * @return {@code true} 이면 구분자로 분리된 형식의 사업자등록번호를 포함
     */
    boolean delimited() default true;

    Strategy[] violations() default {
        CHECKSUM,
        REMOVE,
        ADD,
        MOVE_HYPHEN
    };

    /**
     * 생성할 최대 테스트 케이스 수. 0이면 제한 없음.
     * @return 최대 테스트 케이스 수
     */
    int limit() default 0;

    /**
     * CHECKSUM 전략에서 생성할 잘못된 체크섬 개수
     * @return 체크섬 변형 개수 (기본값: 3)
     */
    int checksumVariations() default 3;
}
