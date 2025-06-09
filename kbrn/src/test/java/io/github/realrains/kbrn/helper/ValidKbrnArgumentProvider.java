package io.github.realrains.kbrn.helper;

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
        int limit = ann.limit();
        
        InputStream is = getClass().getResourceAsStream("/kbrn_sample.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        Stream<Arguments> stream = reader.lines()
            .limit(limit > 0 ? Math.min(limit, 50) : 50) // 최대 50개의 원본 데이터만 사용
            .flatMap(line -> {
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
        
        if (limit > 0 && limit < 100) {
            return stream.limit(limit);
        }
        return stream;
    }
}
