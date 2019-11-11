package com.ralfhenze.rms.railnetworkplanning.domain.common;

public class Preconditions {

    public static <T> T ensureNotNull(T object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException(
                errorMessage + ", but was null"
            );
        }

        return object;
    }

    public static String ensureNotBlank(String string, String errorMessage) {
        if (string == null || string.isEmpty() || string.trim().isEmpty()) {
            throw new IllegalArgumentException(
                errorMessage + ", but was \"" + string + "\""
            );
        }

        return string;
    }
}
