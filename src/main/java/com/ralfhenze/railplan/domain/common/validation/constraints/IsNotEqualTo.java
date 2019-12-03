package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

public class IsNotEqualTo<T> implements ValidationConstraint<T> {

    private final T comparedValue;

    public IsNotEqualTo(final T comparedValue) {
        this.comparedValue = comparedValue;
    }

    @Override
    public boolean isValid(final T value) {
        return !value.equals(comparedValue);
    }

    @Override
    public String getErrorMessage(final String fieldName, final T value) {
        return fieldName + " must not be equal to \"" + value + "\"";
    }
}
