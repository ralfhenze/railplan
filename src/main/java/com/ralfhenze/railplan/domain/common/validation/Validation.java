package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

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

    public Validation() {
        this.rules = Lists.immutable.empty();
    }

    private Validation(final ImmutableList<ValidationRule> rules) {
        this.rules = rules;
    }

    public <T> Validation ensureThat(
        final T value,
        final ValidationConstraint<T> constraint,
        final String fieldName
    ) {
        return new Validation(
            rules.newWith(new ValidationRule(value, constraint, fieldName))
        );
    }

    public Validation throwExceptionIfInvalid() throws ValidationException {
        final var errorMessages = rules
            .select(rule -> !rule.constraint.isValid(rule.value))
            .collect(rule -> rule.constraint.getErrorMessage(rule.fieldName, rule.value));

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }

        return this;
    }
}
