package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.list.ImmutableList;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final ImmutableList<String> errorMessages;

    ValidationException(final ImmutableList<String> errorMessages) {
        super(errorMessages.collect(m -> "\n* " + m).toString());
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages.castToList();
    }
}
