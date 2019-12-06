package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public class ValidationException extends RuntimeException {

    private final ImmutableMap<String, MutableList<String>> errorMessages;

    ValidationException(final ImmutableMap<String, MutableList<String>> errorMessages) {
        super(errorMessages.collect(m -> "\n* " + m).toString());
        this.errorMessages = errorMessages;
    }

    public ImmutableMap<String, MutableList<String>> getErrorMessages() {
        return errorMessages;
    }
}
