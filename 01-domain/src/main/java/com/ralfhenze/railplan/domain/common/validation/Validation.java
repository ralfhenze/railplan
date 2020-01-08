package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.util.Optional;
import java.util.function.Supplier;

public class Validation {

    private final ImmutableList<ValidationRule> rules;
    private final MutableList<ValidationError> errors;

    private static class ValidationRule {
        private final Object value;
        private final ValidationConstraint constraint;
        private final Field field;

        ValidationRule(
            final Object value,
            final ValidationConstraint constraint,
            final Field field
        ) {
            this.value = value;
            this.constraint = constraint;
            this.field = field;
        }
    }

    public Validation() {
        this.rules = Lists.immutable.empty();
        this.errors = Lists.mutable.empty();
    }

    private Validation(
        final ImmutableList<ValidationRule> rules,
        final MutableList<ValidationError> errors
    ) {
        this.rules = rules;
        this.errors = errors;
    }

    public <T> Validation ensureThat(
        final T value,
        final ValidationConstraint<T> constraint,
        final Field field
    ) {
        return new Validation(
            rules.newWith(new ValidationRule(value, constraint, field)),
            errors
        );
    }

    public <T> T get(final Supplier<T> supplier) {
        try {
            final var instance = supplier.get();
            return instance;
        } catch (ValidationException exception) {
            errors.addAll(exception.getValidationErrorsAsList());
            return null;
        }
    }

    /**
     * @throws ValidationException if any ValidationConstraint fails
     */
    public Validation throwExceptionIfInvalid() {
        final ImmutableList errors = rules
            .collect((rule) -> rule.constraint.validate(rule.value)) // TODO: add rule.field
            .select(Optional::isPresent)
            .collect(Optional::get);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return this;
    }
}
