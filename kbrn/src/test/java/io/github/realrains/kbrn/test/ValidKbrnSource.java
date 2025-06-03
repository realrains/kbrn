package io.github.realrains.kbrn.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.CsvFileSource;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CsvFileSource(resources = "/kbrn_sample.csv", numLinesToSkip = 1)
public @interface ValidKbrnSource {
}
