package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;

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
    private final MutableMap<String, MutableList<String>> errorMessages;

    public Validation() {
        this.rules = Lists.immutable.empty();
        this.errorMessages = Maps.mutable.empty();
    }

    private Validation(
        final ImmutableList<ValidationRule> rules,
        final MutableMap<String, MutableList<String>> errorMessages
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
            exception.getErrorMessages().forEachKeyValue((fieldName, messages) ->
                errorMessages
                    .getIfAbsentPut(fieldName, Lists.mutable.empty())
                    .addAll(messages)
            );
            return null;
        }
    }

    public Validation throwExceptionIfInvalid() throws ValidationException {
        final MutableMap<String, MutableList<String>> errors = Maps.mutable.empty();
        for (final var rule : rules) {
            if (!rule.constraint.isValid(rule.value)) {
                errors
                    .getIfAbsentPut(rule.fieldName, Lists.mutable.empty())
                    .add(rule.constraint.getErrorMessage(rule.fieldName, rule.value));
            }
        }
        errorMessages.forEachKeyValue((fieldName, messages) ->
            errors
                .getIfAbsentPut(fieldName, Lists.mutable.empty())
                .addAll(messages)
        );

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toImmutable());
        }

        return this;
    }
}
