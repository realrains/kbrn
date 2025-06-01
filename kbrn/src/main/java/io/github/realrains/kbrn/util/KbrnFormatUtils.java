package io.github.realrains.kbrn.util;

import java.util.regex.Pattern;

public final class KbrnFormatUtils {

    public static final Pattern DEFAULT_KBRN_PATTERN = Pattern.compile("^\\d{10}$");
    public static final Pattern DELIMITED_KBRN_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$");

    private KbrnFormatUtils() {}

    private static boolean isValidFormat(String value, Pattern pattern) {
        return value != null && pattern.matcher(value).matches();
    }

    public static boolean isValidFormat(String value) {
        return isValidFormat(value, DEFAULT_KBRN_PATTERN);
    }

    public static boolean isValidDelimitedFormat(String value) {
        return isValidFormat(value, DELIMITED_KBRN_PATTERN);
    }

    public static String toDelimitedFormat(String value) {
        if (!isValidFormat(value)) {
            throw new IllegalArgumentException("Only default format KBRN can be converted to delimited format: " + value);
        }
        return String.format("%s-%s-%s", value.substring(0, 3), value.substring(3, 5), value.substring(5));
    }

    public static String toDefaultFormat(String value) {
        if (!isValidDelimitedFormat(value)) {
            throw new IllegalArgumentException("Only delimited format KBRN can be converted to default format: " + value);
        }
        return value.replace("-", "");
    }

}
