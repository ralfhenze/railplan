package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.util.function.Supplier;

public class Validation {

    private static class ValidationRule {
        final Object value;
        final ValidationConstraint constraint;
        final String fieldName;

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

    private final ImmutableList<ValidationRule> rules;
    private final MutableList<String> errorMessages;

    public Validation() {
        this.rules = Lists.immutable.empty();
        this.errorMessages = Lists.mutable.empty();
    }

    private Validation(
        final ImmutableList<ValidationRule> rules,
        final MutableList<String> errorMessages
    ) {
        this.rules = rules;
        this.errorMessages = errorMessages;
    }

    public <T> Validation ensureThat(
        final T value,
        final ValidationConstraint<T> constraint,
        final String fieldName
    ) {
        return new Validation(
            rules.newWith(new ValidationRule(value, constraint, fieldName)),
            errorMessages
        );
    }

    public <T> T catchErrors(final Supplier<T> supplier) {
        try {
            final var instance = supplier.get();
            return instance;
        } catch (ValidationException exception) {
            errorMessages.addAll(exception.getErrorMessages());
            return null;
        }
    }

    public Validation throwExceptionIfInvalid() throws ValidationException {
        final var errorMessages = rules
            .select(rule -> !rule.constraint.isValid(rule.value))
            .collect(rule -> rule.constraint.getErrorMessage(rule.fieldName, rule.value))
            .newWithAll(this.errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }

        return this;
    }
}
