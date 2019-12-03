package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

public class HasMinLength implements ValidationConstraint<String> {

    private final int minLength;

    public HasMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public boolean isValid(String value) {
        return (value.length() >= minLength);
    }

    @Override
    public String getErrorMessage(String fieldName, String value) {
        return fieldName + " \"" + value
            + "\" must have a minimum length of " + minLength + " characters";
    }
}
