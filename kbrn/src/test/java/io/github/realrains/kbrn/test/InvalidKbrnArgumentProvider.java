package io.github.realrains.kbrn.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static io.github.realrains.kbrn.test.InvalidKbrnSource.Strategy.CHECKSUM;

public class InvalidKbrnArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        InvalidKbrnSource ann = context.getRequiredTestMethod().getAnnotation(InvalidKbrnSource.class);
        Set<InvalidKbrnSource.Strategy> strategies = Set.of(ann.violations());

        InputStream is = getClass().getResourceAsStream("/kbrn_sample.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        return reader.lines().skip(1).flatMap(line -> {
            String[] cols = line.split(",", -1);
            String plain = cols[0].trim(), delimited = cols[1].trim();
            Stream.Builder<Arguments> streamBuilder = Stream.builder();

            if (strategies.contains(CHECKSUM)) {
                List<String> plains = withInvalidChecksum(plain);
                List<String> delimiteds = withInvalidChecksum(delimited);
                for (int i = 0; i < plains.size(); i++) {
                    String invalidPlain = plains.get(i);
                    String invalidDelimited = delimiteds.get(i);
                    streamBuilder.add(Arguments.of(invalidPlain, invalidDelimited));
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

}
