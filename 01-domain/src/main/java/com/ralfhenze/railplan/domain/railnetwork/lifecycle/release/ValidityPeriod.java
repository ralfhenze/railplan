package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsAfter;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsEqualTo;

import java.time.LocalDate;
import java.util.List;

/**
 * [x] the TimePeriod's StartDate is before (<) EndDate
 */
public class ValidityPeriod implements ValueObject {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate lastEndDate;

    public ValidityPeriod(final LocalDate startDate, final LocalDate endDate) {
        this(startDate, endDate, null);
    }

    public ValidityPeriod(
        final LocalDate startDate,
        final LocalDate endDate,
        final LocalDate lastEndDate
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastEndDate = lastEndDate;
    }

    @Override
    public boolean isValid() {
        return getStartDateErrors().isEmpty()
            && getEndDateErrors().isEmpty();
    }

    public List<ValidationError> getStartDateErrors() {
        if (lastEndDate != null) {
            return new Validation<>(startDate)
                .ensureIt(new IsEqualTo<>(lastEndDate.plusDays(1)))
                .getValidationErrors();
        } else {
            return List.of();
        }
    }

    public List<ValidationError> getEndDateErrors() {
        return new Validation<>(endDate)
            .ensureIt(new IsAfter(startDate))
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
