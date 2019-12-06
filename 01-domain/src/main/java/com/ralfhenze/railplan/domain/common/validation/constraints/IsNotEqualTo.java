package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;

public class IsNotEqualTo<T> implements ValidationConstraint<T> {

    private final T comparedValue;

    public IsNotEqualTo(final T comparedValue) {
        this.comparedValue = comparedValue;
    }

    @Override
    public Optional<ErrorMessage> validate(final T value, final String fieldName) {
        if (value.equals(comparedValue)) {
            return Optional.of(
                new ErrorMessage(fieldName + " must not be equal to \"" + value + "\"")
            );
        }

        return Optional.empty();
    }
}
