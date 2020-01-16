package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import java.util.Optional;
import java.util.function.Supplier;

public class Validation {

    private final MutableList<ValidationRule> rules;
    private final MutableList<ValidationError> errors;

    private static class ValidationRule {
        private Object value;
        private ValidationConstraint constraint;
        private Field field;
    }

    public Validation() {
        this.rules = Lists.mutable.empty();
        this.errors = Lists.mutable.empty();
    }

    public <T> Validation ensureThat(
        final T value,
        final ValidationConstraint<T> constraint,
        final Field field
    ) {
        final var rule = new ValidationRule();
        rule.value = value;
        rule.constraint = constraint;
        rule.field = field;

        rules.add(rule);

        return this;
    }

    public <T> T get(final Supplier<T> supplier) {
        try {
            final var instance = supplier.get();
            return instance;
        } catch (ValidationException exception) {
            errors.addAll(exception.getValidationErrors());
            return null;
        }
    }

    /**
     * @throws ValidationException if any ValidationConstraint fails
     */
    public Validation throwExceptionIfInvalid() {
        final MutableList collectedErrors = rules
            .collect((rule) -> rule.constraint.validate(rule.value, rule.field))
            .select(Optional::isPresent)
            .collect(Optional::get)
            .toList();

        collectedErrors.addAll(errors);

        if (!collectedErrors.isEmpty()) {
            throw new ValidationException(collectedErrors);
        }

        return this;
    }
}
