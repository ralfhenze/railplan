package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;

public class HasMinLength implements ValidationConstraint<String> {

    private final int minLength;

    public HasMinLength(final int minLength) {
        this.minLength = minLength;
    }

    @Override
    public Optional<ErrorMessage> validate(final String value, final String fieldName) {
        if (value.length() < minLength) {
            return Optional.of(
                new ErrorMessage(
                    fieldName + " \"" + value
                    + "\" must have a minimum length of " + minLength + " characters"
                )
            );
        }

        return Optional.empty();
    }
}
