package io.github.realrains.kbrn.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ValidKbrnArgumentProvider.class)
public @interface ValidKbrnSource {
    boolean plain() default true;
    boolean delimited() default true;
}
