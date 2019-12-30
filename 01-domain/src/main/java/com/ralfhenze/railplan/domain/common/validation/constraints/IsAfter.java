package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.time.LocalDate;
import java.util.Optional;

public class IsAfter implements ValidationConstraint<LocalDate> {

    private final LocalDate startDate;

    public IsAfter(final LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public Optional<ErrorMessage> validate(
        final LocalDate date,
        final String fieldName
    ) {
        if (!date.isAfter(startDate)) {
            return Optional.of(new ErrorMessage(
                fieldName + " (" + date + ") must be after " + startDate
            ));
        }

        return Optional.empty();
    }
}
