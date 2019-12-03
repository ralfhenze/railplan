package com.ralfhenze.railplan.domain.common.validation;

import java.util.Optional;

public interface ValidationConstraint<T> {
    Optional<ErrorMessage> validate(final T value, final String fieldName);
}
