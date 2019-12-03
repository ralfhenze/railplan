package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;

public class IsNotNull<T> implements ValidationConstraint<T> {

    @Override
    public Optional<ErrorMessage> validate(final T value, final String fieldName) {
        if (value == null) {
            return Optional.of(
                new ErrorMessage(fieldName + " is required, but was null")
            );
        }

        return Optional.empty();
    }
}
