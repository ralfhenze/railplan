package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;

public class IsWithinRange implements ValidationConstraint<Double> {

    private final Double inclusiveMin;
    private final Double inclusiveMax;

    public IsWithinRange(final Double inclusiveMin, final Double inclusiveMax) {
        this.inclusiveMin = inclusiveMin;
        this.inclusiveMax = inclusiveMax;
    }

    @Override
    public Optional<ErrorMessage> validate(final Double value, final String fieldName) {
        if (value < inclusiveMin || value > inclusiveMax) {
            return Optional.of(
                new ErrorMessage(
                    fieldName + " must be within "
                    + "[" + inclusiveMin + " ... " + inclusiveMax + "]"
                    + ", but was " + value
                )
            );
        }

        return Optional.empty();
    }
}