package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import org.eclipse.collections.api.collection.ImmutableCollection;

import java.util.Optional;

public class HasMinSize<T extends ImmutableCollection> implements ValidationConstraint<T> {

    private final int minSize;

    public HasMinSize(final int minSize) {
        this.minSize = minSize;
    }

    @Override
    public Optional<ValidationError> validate(final T collection, final Field field) {
        if (collection.size() < minSize) {
            return Optional.of(
                new ValidationError("requires at least " + minSize + " elements", field)
            );
        }

        return Optional.empty();
    }
}
