package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.Optional;
import java.util.regex.Pattern;

public class MatchesRegex implements ValidationConstraint<String> {

    private final String regexPattern;

    public MatchesRegex(final String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public Optional<ErrorMessage> validate(final String value, final String fieldName) {
        if (!Pattern.matches(regexPattern, value)) {
            return Optional.of(
                new ErrorMessage(
                    fieldName + " \"" + value
                    + "\" doesn't match regular expression \"" + regexPattern + "\""
                )
            );
        }

        return Optional.empty();
    }
}
