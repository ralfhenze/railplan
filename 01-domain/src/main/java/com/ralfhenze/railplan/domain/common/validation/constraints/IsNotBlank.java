package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;

public class IsNotBlank implements ValidationConstraint<String> {

    @Override
    public Optional<ErrorMessage> validate(final String value, final String fieldName) {
        if (value.isBlank()) {
            return Optional.of(new ErrorMessage(
                fieldName + " must not be blank, but was \"" + value + "\""
            ));
        }

        return Optional.empty();
    }
}
