package com.ralfhenze.railplan.domain.common;

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

        if (string.isBlank()) {
            throw new IllegalArgumentException(
                parameterName + " must not be blank, but was \"" + string + "\""
            );
        }

        return string;
    }
}
