package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class IsNotBlank implements ValidationConstraint<String> {

    @Override
    public Optional<ValidationError> validate(final String value, final Field field) {
        if (value.isBlank()) {
            return Optional.of(new ValidationError("must not be blank", field));
        }

        return Optional.empty();
    }
}
