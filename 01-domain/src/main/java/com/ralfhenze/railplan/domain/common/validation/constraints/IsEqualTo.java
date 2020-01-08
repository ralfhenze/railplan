package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class IsEqualTo<T> implements ValidationConstraint<T> {

    private final T comparedValue;

    public IsEqualTo(final T comparedValue) {
        this.comparedValue = comparedValue;
    }

    @Override
    public Optional<ValidationError> validate(final T value, final Field field) {
        if (!value.equals(comparedValue)) {
            return Optional.of(new ValidationError("must be equal to \"" + value + "\""));
        }

        return Optional.empty();
    }
}
