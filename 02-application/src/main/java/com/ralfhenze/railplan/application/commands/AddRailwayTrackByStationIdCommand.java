package com.ralfhenze.railplan.application.commands;

public class AddRailwayTrackByStationIdCommand implements Command {

    private final String draftId;
    private final int firstStationId;
    private final int secondStationId;

    public AddRailwayTrackByStationIdCommand(
        final String draftId,
        final int firstStationId,
        final int secondStationId
    ) {
        this.draftId = draftId;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
    }

    public String getDraftId() {
        return draftId;
    }

    public int getFirstStationId() {
        return firstStationId;
    }

    public int getSecondStationId() {
        return secondStationId;
    }
}
