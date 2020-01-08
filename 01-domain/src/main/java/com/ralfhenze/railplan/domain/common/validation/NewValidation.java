package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.util.Optional;
import java.util.function.Supplier;

public class NewValidation {

    private final ImmutableList<ValidationRule> rules;
    private final MutableList<ValidationError> errors;

    private static class ValidationRule {
        private final Object value;
        private final ValidationConstraint constraint;
        private final String fieldName;

        ValidationRule(
            final Object value,
            final ValidationConstraint constraint,
            final String fieldName
        ) {
            this.value = value;
            this.constraint = constraint;
            this.fieldName = fieldName;
        }
    }

    public NewValidation() {
        this.rules = Lists.immutable.empty();
        this.errors = Lists.mutable.empty();
    }

    private NewValidation(
        final ImmutableList<ValidationRule> rules,
        final MutableList<ValidationError> errors
    ) {
        this.rules = rules;
        this.errors = errors;
    }

    public <T> NewValidation ensureThat(
        final T value,
        final ValidationConstraint<T> constraint,
        final String fieldName
    ) {
        return new NewValidation(
            rules.newWith(new ValidationRule(value, constraint, fieldName)),
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
    public NewValidation throwExceptionIfInvalid() {
        final ImmutableList errors = rules
            .collect((rule) -> rule.constraint.validate(rule.value)) // TODO: add rule.fieldName
            .select(Optional::isPresent)
            .collect(Optional::get);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return this;
    }
}
