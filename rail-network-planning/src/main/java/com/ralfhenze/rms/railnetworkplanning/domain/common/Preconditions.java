package com.ralfhenze.rms.railnetworkplanning.domain.common;

public class Preconditions {

    public static void assertNotNull(Object object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException(
                errorMessage + ", but was null"
            );
        }
    }

    public static void assertNotBlank(String string, String errorMessage) {
        if (string == null || string.isEmpty() || string.trim().isEmpty()) {
            throw new IllegalArgumentException(
                errorMessage + ", but was \"" + string + "\""
            );
        }
    }
}
