package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import java.time.LocalDate;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the TimePeriod's StartDate is before (<) EndDate
 */
public class ValidityPeriod implements ValueObject {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public ValidityPeriod(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = ensureNotNull(startDate, "Start Date");
        this.endDate = ensureNotNull(endDate, "End Date");

        ensureStartDateIsBeforeEndDate();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    private void ensureStartDateIsBeforeEndDate() {
        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException(
                "Start date (" + startDate + ") must be before end date (" + endDate + ")"
            );
        }
    }

    @Override
    public String toString() {
        return "Start: " + startDate.toString() + " End: " + endDate.toString();
    }
}
