package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class IsPositive implements ValidationConstraint<Integer> {

    @Override
    public Optional<ValidationError> validate(final Integer value, final Field field) {
        if (value <= 0) {
            return Optional.of(new ValidationError("must be positive (> 0)", field));
        }

        return Optional.empty();
    }
}
