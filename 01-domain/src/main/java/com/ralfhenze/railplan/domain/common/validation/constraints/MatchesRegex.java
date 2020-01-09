package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.Optional;
import java.util.regex.Pattern;

public class MatchesRegex implements ValidationConstraint<String> {

    private final String regexPattern;

    public MatchesRegex(final String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public Optional<ValidationError> validate(final String value, final Field field) {
        if (!Pattern.matches(regexPattern, value)) {
            return Optional.of(
                new ValidationError(
                    "must match regular expression \"" + regexPattern + "\"", field
                )
            );
        }

        return Optional.empty();
    }
}
