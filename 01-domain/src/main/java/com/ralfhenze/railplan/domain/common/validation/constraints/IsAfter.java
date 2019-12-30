package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.time.LocalDate;
import java.util.Optional;

public class IsAfter implements ValidationConstraint<LocalDate> {

    private final LocalDate startDate;

    public IsAfter(final LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public Optional<ValidationError> validate(final LocalDate date) {
        if (!date.isAfter(startDate)) {
            return Optional.of(new ValidationError("must be after " + startDate));
        }

        return Optional.empty();
    }
}
