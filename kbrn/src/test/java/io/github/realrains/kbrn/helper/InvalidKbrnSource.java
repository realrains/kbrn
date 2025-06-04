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
}
