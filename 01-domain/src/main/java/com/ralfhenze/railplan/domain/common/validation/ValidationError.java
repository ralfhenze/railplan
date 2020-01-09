package com.ralfhenze.railplan.domain.common.validation;

public class ValidationError {

    private final String message;

    public ValidationError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
