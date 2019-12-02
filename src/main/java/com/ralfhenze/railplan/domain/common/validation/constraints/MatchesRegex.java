package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.util.regex.Pattern;

public class MatchesRegex implements ValidationConstraint<String> {

    private final String regexPattern;

    public MatchesRegex(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public boolean isValid(String value) {
        return Pattern.matches(regexPattern, value);
    }

    @Override
    public String getErrorMessage(String fieldName, String value) {
        return fieldName + " \"" + value
            + "\" doesn't match regular expression \"" + regexPattern + "\"";
    }
}
