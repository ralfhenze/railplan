package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

public class IsNotNull<T> implements ValidationConstraint<T> {

    @Override
    public boolean isValid(final T value) {
        return (value != null);
    }

    @Override
    public String getErrorMessage(final String fieldName, final T value) {
        return fieldName + " is required, but was null";
    }
}
