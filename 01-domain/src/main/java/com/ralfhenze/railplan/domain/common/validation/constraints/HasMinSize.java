package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import org.eclipse.collections.api.collection.ImmutableCollection;

import java.util.Optional;

public class HasMinSize<T extends ImmutableCollection> implements ValidationConstraint<T> {

    private final int minSize;

    public HasMinSize(final int minSize) {
        this.minSize = minSize;
    }

    @Override
    public Optional<ErrorMessage> validate(
        final T collection,
        final String fieldName
    ) {
        if (collection.size() < minSize) {
            return Optional.of(new ErrorMessage(
                fieldName + " requires at least "
                + minSize + " elements, but had " + collection.size()
            ));
        }

        return Optional.empty();
    }
}
