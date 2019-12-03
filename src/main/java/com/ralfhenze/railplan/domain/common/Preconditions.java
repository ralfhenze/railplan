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
}
