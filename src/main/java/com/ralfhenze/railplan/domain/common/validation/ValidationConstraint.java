package com.ralfhenze.railplan.domain.common.validation;

public interface ValidationConstraint<T> {
    boolean isValid(final T value);
    String getErrorMessage(final String fieldName, final T value);
}
