package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;

public class HasMinLength implements ValidationConstraint<String> {

    private final int minLength;

    public HasMinLength(final int minLength) {
        this.minLength = minLength;
    }

    @Override
    public Optional<ValidationError> validate(final String value) {
        if (value.length() < minLength) {
            return Optional.of(
                new ValidationError("must have a minimum length of " + minLength + " characters")
            );
        }

        return Optional.empty();
    }
}
