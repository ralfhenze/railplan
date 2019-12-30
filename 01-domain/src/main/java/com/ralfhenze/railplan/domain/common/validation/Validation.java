package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Validation<T> {

    private final T property;

    private final ImmutableList<ValidationConstraint<T>> constraints;

    public Validation(final T property) {
        this(property, Lists.immutable.empty());
    }

    private Validation(
        final T property,
        final ImmutableList<ValidationConstraint<T>> constraints
    ) {
        this.property = property;
        this.constraints = constraints;
    }

    public Validation<T> ensureIt(
        final ValidationConstraint<T> constraint
    ) {
        return new Validation<>(
            property, constraints.newWith(constraint)
        );
    }

    public List<ValidationError> getValidationErrors() {
        final List<ValidationError> errors = new ArrayList<>();

        for (final var constraint : constraints) {
            constraint.validate(property).ifPresent(errors::add);
        }

        return errors;
    }
}
