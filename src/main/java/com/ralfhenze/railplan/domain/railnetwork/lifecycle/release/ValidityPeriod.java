package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsBefore;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotNull;

import java.time.LocalDate;

/**
 * [x] the TimePeriod's StartDate is before (<) EndDate
 */
public class ValidityPeriod implements ValueObject {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public ValidityPeriod(final LocalDate startDate, final LocalDate endDate) {
        new Validation()
            .ensureThat(startDate, new IsNotNull<>(), "Start Date")
            .ensureThat(endDate, new IsNotNull<>(), "End Date")
            .ensureThat(startDate, new IsBefore(endDate), "Start Date")
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
