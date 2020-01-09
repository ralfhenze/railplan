package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class IsNotNull implements ValidationConstraint<Object> {

    @Override
    public Optional<ValidationError> validate(final Object value, final Field field) {
        if (value == null) {
            return Optional.of(new ValidationError("must not be null", field));
        }

        return Optional.empty();
    }
}
