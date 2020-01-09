package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsAfter;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsEqualTo;

import java.time.LocalDate;

/**
 * [x] the TimePeriod's StartDate is before (<) EndDate
 */
public class ValidityPeriod implements ValueObject {

    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * @throws ValidationException if startDate is after endDate
     */
    public ValidityPeriod(final LocalDate startDate, final LocalDate endDate) {
        new Validation()
            .ensureThat(endDate, new IsAfter(startDate), Field.END_DATE)
            .throwExceptionIfInvalid();

        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @throws ValidationException if startDate is after endDate or not one day after lastEndDate
     */
    public ValidityPeriod(
        final LocalDate startDate,
        final LocalDate endDate,
        final LocalDate lastEndDate
    ) {
        new Validation()
            .ensureThat(startDate, new IsEqualTo<>(lastEndDate.plusDays(1)), Field.START_DATE)
            .ensureThat(endDate, new IsAfter(startDate), Field.END_DATE)
            .throwExceptionIfInvalid();

        this.startDate = startDate;
        this.endDate = endDate;
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
