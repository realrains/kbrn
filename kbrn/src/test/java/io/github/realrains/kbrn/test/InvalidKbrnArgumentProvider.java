package io.github.realrains.kbrn.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.ADD;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.CHECKSUM;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.MOVE_HYPHEN;
import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.REMOVE;
import static java.util.Objects.requireNonNull;

public class InvalidKbrnArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        InvalidKbrnSource ann = requireNonNull(context.getRequiredTestMethod().getAnnotation(InvalidKbrnSource.class));
        boolean plain = ann.plain();
        boolean delimited = ann.delimited();
        Set<InvalidKbrnSource.Strategy> strategies = Set.of(ann.violations());

        InputStream is = getClass().getResourceAsStream("/kbrn_sample.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        return reader.lines().flatMap(line -> {
            String value = line.trim();
            Stream.Builder<Arguments> streamBuilder = Stream.builder();

            if (strategies.contains(CHECKSUM)) {
                List<String> invalidValues = withInvalidChecksum(value);
                if (plain) {
                    invalidValues.forEach(it -> streamBuilder.add(Arguments.of(it.replace("-", ""))));
                }
                if (delimited) {
                    invalidValues.forEach(it -> streamBuilder.add(Arguments.of(it)));
                }
            }

            if (strategies.contains(REMOVE)) {
                if (plain) {
                    streamBuilder.add(Arguments.of(remove(value.replace("-", ""))));
                }
                if (delimited) {
                    streamBuilder.add(Arguments.of(remove(value)));
                }
            }

            if (strategies.contains(ADD)) {
                if (plain) {
                    streamBuilder.add(Arguments.of(add(value.replace("-", ""))));
                }
                if (delimited) {
                    streamBuilder.add(Arguments.of(add(value)));
                }
            }

            if (strategies.contains(MOVE_HYPHEN)) {
                if (delimited) {
                    String invalid = moveHyphen(value);
                    if (invalid != null) {
                        streamBuilder.add(Arguments.of(invalid));
                    }
                }
            }

            return streamBuilder.build();
        });
    }

    private static List<String> withInvalidChecksum(String value) {
        ArrayList<String> results = new ArrayList<>();
        char lastChar = value.charAt(value.length() - 1);
        for (char c = '0'; c <= '9'; c++) {
            if (c != lastChar) {
                results.add(value.substring(0, value.length() - 1) + c);
            }
        }
        return results;
    }

    // 무작위 위치에서 제거
    private static String remove(String value) {
        int index = Math.abs(value.hashCode()) % value.length();
        return value.substring(0, index) + value.substring(index + 1);
    }

    // 무작위 위치에 숫자 추가
    private static String add(String value) {
        int index = Math.abs(value.hashCode()) % value.length();
        char c = (char) ('0' + Math.abs(value.hashCode()) % 10);
        return value.substring(0, index) + c + value.substring(index);
    }

    // 하이픈을 무작위 위치로 이동
    private static String moveHyphen(String value) {
        if (value.indexOf('-') < 0) {
            return value;
        }

        String result = value.replace("-", "");

        for (int i = 0; i < 2; i++) {
            int index = Math.abs(result.hashCode() << result.length()) % result.length();
            result = result.substring(0, index) + '-' + result.substring(index);
        }

        if (result.equals(value)) {
            return null;
        }

        return result;
    }

}
