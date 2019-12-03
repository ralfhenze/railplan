package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;

public class IsNotNull implements ValidationConstraint<Object> {

    @Override
    public Optional<ErrorMessage> validate(final Object value, final String fieldName) {
        if (value == null) {
            return Optional.of(
                new ErrorMessage(fieldName + " is required, but was null")
            );
        }

        return Optional.empty();
    }
}
