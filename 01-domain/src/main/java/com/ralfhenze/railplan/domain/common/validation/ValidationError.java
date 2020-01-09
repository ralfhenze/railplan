package com.ralfhenze.railplan.domain.common.validation;

public class ValidationError {

    private final String message;
    private final Field field;

    public ValidationError(final String message, final Field field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return field.toString() + ": " + message;
    }
}
