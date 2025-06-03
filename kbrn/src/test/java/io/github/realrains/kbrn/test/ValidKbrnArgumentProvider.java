package io.github.realrains.kbrn.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static java.util.Objects.requireNonNull;

public class ValidKbrnArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        ValidKbrnSource ann = requireNonNull(context.getRequiredTestMethod().getAnnotation(ValidKbrnSource.class));
        boolean plain = ann.plain();
        boolean delimited = ann.delimited();
        InputStream is = getClass().getResourceAsStream("/kbrn_sample.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        return reader.lines().flatMap(line -> {
            String value = line.trim();
            Stream.Builder<Arguments> streamBuilder = Stream.builder();

            if (plain) {
                streamBuilder.add(Arguments.of(value.replace("-", "")));
            }
            if (delimited) {
                streamBuilder.add(Arguments.of(value));
            }

            return streamBuilder.build();
        });
    }
}
