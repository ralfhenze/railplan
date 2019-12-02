package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

public class IsWithinRange implements ValidationConstraint<Double> {

    private final Double inclusiveMin;
    private final Double inclusiveMax;

    public IsWithinRange(Double inclusiveMin, Double inclusiveMax) {
        this.inclusiveMin = inclusiveMin;
        this.inclusiveMax = inclusiveMax;
    }

    @Override
    public boolean isValid(Double value) {
        return (value >= inclusiveMin && value <= inclusiveMax);
    }

    @Override
    public String getErrorMessage(String fieldName, Double value) {
        return fieldName + " must be within "
            + "[" + inclusiveMin + " ... " + inclusiveMax + "]"
            + ", but was " + value;
    }
}
