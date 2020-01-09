package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class IsNotEqualTo<T> implements ValidationConstraint<T> {

    private final T comparedValue;

    public IsNotEqualTo(final T comparedValue) {
        this.comparedValue = comparedValue;
    }

    @Override
    public Optional<ValidationError> validate(final T value, final Field field) {
        if (value.equals(comparedValue)) {
            return Optional.of(
                new ValidationError("must not be equal to \"" + value + "\"", field)
            );
        }

        return Optional.empty();
    }
}
