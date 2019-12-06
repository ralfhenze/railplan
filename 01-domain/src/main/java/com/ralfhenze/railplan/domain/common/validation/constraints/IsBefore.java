package com.ralfhenze.railplan.domain.common.validation.constraints;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;

import java.time.LocalDate;
import java.util.Optional;

public class IsBefore implements ValidationConstraint<LocalDate> {

    private final LocalDate laterDate;

    public IsBefore(final LocalDate laterDate) {
        this.laterDate = laterDate;
    }

    @Override
    public Optional<ErrorMessage> validate(
        final LocalDate date,
        final String fieldName
    ) {
        if (!date.isBefore(laterDate)) {
            return Optional.of(new ErrorMessage(
                fieldName + " (" + date + ") must be before " + laterDate
            ));
        }

        return Optional.empty();
    }
}
