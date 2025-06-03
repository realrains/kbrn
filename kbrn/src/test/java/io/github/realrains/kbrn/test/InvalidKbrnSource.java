package io.github.realrains.kbrn.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.ADD;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.CHECKSUM;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.MOVE_HYPHEN;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.REMOVE;

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

    boolean plain() default true;

    boolean delimited() default true;

    Strategy[] violations() default {
        CHECKSUM,
        REMOVE,
        ADD,
        MOVE_HYPHEN
    };
}
