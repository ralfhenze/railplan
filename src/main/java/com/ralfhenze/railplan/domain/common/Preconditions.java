package com.ralfhenze.railplan.domain.common;

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

    public static <T> void ensureNotEqual(T object1, T object2, String parameterName1, String parameterName2) {
        if (object1.equals(object2)) {
            throw new IllegalArgumentException(
                parameterName1 + " and " + parameterName2 + " must not be equal"
            );
        }
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
