package com.ralfhenze.railplan.application.commands;

import java.time.LocalDate;

/**
 * A command DTO to release a RailNetworkDraft.
 */
public class ReleaseRailNetworkCommand implements Command {

    private final String draftId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ReleaseRailNetworkCommand(
        final String draftId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        this.draftId = draftId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDraftId() {
        return draftId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
