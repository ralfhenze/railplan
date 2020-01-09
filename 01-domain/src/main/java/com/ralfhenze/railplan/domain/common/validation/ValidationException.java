package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final ImmutableList<ValidationError> validationErrors;

    public ValidationException(final List<ValidationError> validationErrors) {
        this(Lists.immutable.ofAll(validationErrors));
    }

    public ValidationException(final ImmutableList<ValidationError> validationErrors) {
        super(validationErrors.collect(m -> "\n* " + m).toString());
        this.validationErrors = validationErrors;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors.castToList();
    }

    public List<String> getValidationErrorsOfField(final Field field) {
        return validationErrors
            .select((error) -> error.getField().equals(field))
            .collect(ValidationError::getMessage)
            .castToList();
    }
}
