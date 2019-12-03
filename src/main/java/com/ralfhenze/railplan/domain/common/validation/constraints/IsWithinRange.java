package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

public class IsWithinRange implements ValidationConstraint<Double> {

    private final Double inclusiveMin;
    private final Double inclusiveMax;

    public IsWithinRange(final Double inclusiveMin, final Double inclusiveMax) {
        this.inclusiveMin = inclusiveMin;
        this.inclusiveMax = inclusiveMax;
    }

    @Override
    public boolean isValid(final Double value) {
        return (value >= inclusiveMin && value <= inclusiveMax);
    }

    @Override
    public String getErrorMessage(final String fieldName, final Double value) {
        return fieldName + " must be within "
            + "[" + inclusiveMin + " ... " + inclusiveMax + "]"
            + ", but was " + value;
    }
}
