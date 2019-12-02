package com.ralfhenze.railplan.domain.common.validation;

public interface ValidationConstraint<T> {
    boolean isValid(T value);
    String getErrorMessage(String fieldName, T value);
}
