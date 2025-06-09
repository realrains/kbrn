package io.github.realrains.kbrn.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.ADD;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.CHECKSUM;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.MOVE_HYPHEN;
import static io.github.realrains.kbrn.helper.InvalidKbrnSource.Strategy.REMOVE;
import static java.util.Objects.requireNonNull;

public class InvalidKbrnArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        InvalidKbrnSource ann = requireNonNull(context.getRequiredTestMethod().getAnnotation(InvalidKbrnSource.class));
        boolean plain = ann.plain();
        boolean delimited = ann.delimited();
        Set<InvalidKbrnSource.Strategy> strategies = Set.of(ann.violations());
        int limit = ann.limit();
        int checksumVariations = ann.checksumVariations();

        InputStream is = getClass().getResourceAsStream("/kbrn_sample.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        AtomicInteger counter = new AtomicInteger(0);
        Stream<Arguments> stream = reader.lines()
            .limit(limit > 0 ? Math.min(limit, 30) : 30) // 최대 30개의 원본 데이터만 사용
            .flatMap(line -> {
            String value = line.trim();
            Stream.Builder<Arguments> streamBuilder = Stream.builder();

            if (strategies.contains(CHECKSUM)) {
                List<String> invalidValues = withInvalidChecksum(value, checksumVariations);
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
        
        if (limit > 0) {
            return stream.limit(limit);
        }
        return stream;
    }

    private static List<String> withInvalidChecksum(String value, int variations) {
        ArrayList<String> results = new ArrayList<>();
        char lastChar = value.charAt(value.length() - 1);
        int lastDigit = lastChar - '0';
        
        // 원본 체크섬의 ±1, ±2 등 근처 값들로 제한
        int added = 0;
        for (int offset = 1; offset <= 9 && added < variations; offset++) {
            int newDigit = (lastDigit + offset) % 10;
            char newChar = (char) ('0' + newDigit);
            if (newChar != lastChar) {
                results.add(value.substring(0, value.length() - 1) + newChar);
                added++;
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

    // 하이픈을 잘못된 위치로 이동 (단순화)
    private static String moveHyphen(String value) {
        if (!value.contains("-")) {
            return value;
        }

        String plain = value.replace("-", "");
        // 잘못된 하이픈 위치 예시: 12-081-47521, 1208-147-521
        int hash = Math.abs(value.hashCode());
        int[] wrongPositions = {2, 5, 4, 7}; // 가능한 잘못된 위치들
        int position = wrongPositions[hash % wrongPositions.length];
        
        if (position >= plain.length()) {
            position = plain.length() - 1;
        }
        
        String result = plain.substring(0, position) + '-' + plain.substring(position);
        // 두 번째 하이픈 추가
        int secondPos = position + 4;
        if (secondPos < result.length()) {
            result = result.substring(0, secondPos) + '-' + result.substring(secondPos);
        }
        
        return result.equals(value) ? null : result;
    }

}
