package com.ralfhenze.railplan.domain.common.validation;

import java.util.Optional;

public interface ValidationConstraint<T> {
    Optional<ValidationError> validate(final T value, final Field field);
}
