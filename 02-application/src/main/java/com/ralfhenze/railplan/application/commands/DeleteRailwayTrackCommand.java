package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a RailwayTrack from a RailNetworkDraft.
 */
public class DeleteRailwayTrackCommand implements Command {

    private final String draftId;
    private final String stationId1;
    private final String stationId2;

    public DeleteRailwayTrackCommand(
        final String draftId,
        final String stationId1,
        final String stationId2
    ) {
        this.draftId = draftId;
        this.stationId1 = stationId1;
        this.stationId2 = stationId2;
    }

    public String getDraftId() {
        return draftId;
    }

    public String getStationId1() {
        return stationId1;
    }

    public String getStationId2() {
        return stationId2;
    }
}
