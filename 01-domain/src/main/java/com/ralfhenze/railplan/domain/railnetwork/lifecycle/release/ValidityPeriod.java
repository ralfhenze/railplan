package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Validatable;
import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.PropertyValidation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsBefore;

import java.time.LocalDate;
import java.util.List;

/**
 * [x] the TimePeriod's StartDate is before (<) EndDate
 */
public class ValidityPeriod implements ValueObject, Validatable {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public ValidityPeriod(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean isValid() {
        return getStartDateErrors().isEmpty();
    }

    public List<ValidationError> getStartDateErrors() {
        return new PropertyValidation<>(startDate)
            .ensureIt(new IsBefore(endDate))
            .getValidationErrors();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Start: " + startDate.toString() + " End: " + endDate.toString();
    }
}
