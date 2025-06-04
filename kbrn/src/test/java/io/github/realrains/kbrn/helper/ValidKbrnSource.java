package io.github.realrains.kbrn.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * 유효한 사업자등록번호를 제공하는 JUnit 5 어노테이션
 * <p>
 * 이 어노테이션은 {@link ValidKbrnArgumentProvider}를 사용하여 테스트 메서드에 유효한 사업자등록번호를 제공합니다.
 * 기본 형식과 구분자로 분리된 형식의 사업자등록번호를 모두 포함할 수 있습니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ValidKbrnArgumentProvider.class)
public @interface ValidKbrnSource {
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
}
