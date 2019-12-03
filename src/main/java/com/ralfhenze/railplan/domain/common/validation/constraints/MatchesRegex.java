package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.regex.Pattern;

public class MatchesRegex implements ValidationConstraint<String> {

    private final String regexPattern;

    public MatchesRegex(final String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public boolean isValid(final String value) {
        return Pattern.matches(regexPattern, value);
    }

    @Override
    public String getErrorMessage(final String fieldName, final String value) {
        return fieldName + " \"" + value
            + "\" doesn't match regular expression \"" + regexPattern + "\"";
    }
}
