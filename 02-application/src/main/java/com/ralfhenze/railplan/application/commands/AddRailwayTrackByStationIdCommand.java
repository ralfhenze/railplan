package com.ralfhenze.railplan.application.commands;

public class AddRailwayTrackByStationIdCommand implements Command {

    private final String draftId;
    private final String firstStationId;
    private final String secondStationId;

    public AddRailwayTrackByStationIdCommand(
        final String draftId,
        final String firstStationId,
        final String secondStationId
    ) {
        this.draftId = draftId;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
    }

    public String getDraftId() {
        return draftId;
    }

    public String getFirstStationId() {
        return firstStationId;
    }

    public String getSecondStationId() {
        return secondStationId;
    }
}
