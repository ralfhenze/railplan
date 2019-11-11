package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import java.time.LocalDate;

/**
 * [ ] the TimePeriod's minimum duration is 6 months
 * [ ] the TimePeriod's StartDate is before (<) EndDate
 *     -> Smart Constructor
 */
class RailNetworkPeriod implements ValueObject {

    private final LocalDate startDate;
    private final LocalDate endDate;

    RailNetworkPeriod(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
