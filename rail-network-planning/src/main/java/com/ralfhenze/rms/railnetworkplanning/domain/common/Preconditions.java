package com.ralfhenze.rms.railnetworkplanning.domain.common;

import java.util.regex.Pattern;

public class Preconditions {

    public static <T> T ensureNotNull(T object, String parameterName) {
        if (object == null) {
            throw new IllegalArgumentException(
                parameterName + " is required, but was null"
            );
        }

        return object;
    }

    public static String ensureNotBlank(String string, String parameterName) {
        ensureNotNull(string, parameterName);

        if (string.isEmpty() || string.trim().isEmpty()) {
            throw new IllegalArgumentException(
                parameterName + " must not be blank, but was \"" + string + "\""
            );
        }

        return string;
    }

    public static String ensureRegexMatch(String string, String regexPattern, String parameterName) {
        ensureNotNull(string, parameterName);

        if (!Pattern.matches(regexPattern, string)) {
            throw new IllegalArgumentException(
                parameterName + " \"" + string
                    + "\" doesn't match regular expression \"" + regexPattern + "\""
            );
        }

        return string;
    }

    public static double ensureWithinRange(double value, double min, double max, String parameterName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                parameterName + " must be within [" + min + " ... " + max + "], but was " + value
            );
        }

        return value;
    }
}
