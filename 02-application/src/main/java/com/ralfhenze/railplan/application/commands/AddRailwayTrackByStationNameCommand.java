package com.ralfhenze.railplan.application.commands;

public class AddRailwayTrackByStationNameCommand implements Command {

    private final String draftId;
    private final String firstStationName;
    private final String secondStationName;

    public AddRailwayTrackByStationNameCommand(
        final String draftId,
        final String firstStationName,
        final String secondStationName
    ) {
        this.draftId = draftId;
        this.firstStationName = firstStationName;
        this.secondStationName = secondStationName;
    }

    public String getDraftId() {
        return draftId;
    }

    public String getFirstStationName() {
        return firstStationName;
    }

    public String getSecondStationName() {
        return secondStationName;
    }
}
