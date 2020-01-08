package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class IsWithinRange implements ValidationConstraint<Double> {

    private final Double inclusiveMin;
    private final Double inclusiveMax;

    public IsWithinRange(final Double inclusiveMin, final Double inclusiveMax) {
        this.inclusiveMin = inclusiveMin;
        this.inclusiveMax = inclusiveMax;
    }

    @Override
    public Optional<ValidationError> validate(final Double value, final Field field) {
        if (value < inclusiveMin || value > inclusiveMax) {
            return Optional.of(
                new ValidationError(
                    "must be within [" + inclusiveMin + " ... " + inclusiveMax + "]"
                )
            );
        }

        return Optional.empty();
    }
}
