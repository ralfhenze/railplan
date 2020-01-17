package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a RailwayTrack from a RailNetworkDraft.
 */
public class DeleteRailwayTrackCommand implements Command {

    private final String draftId;
    private final int firstStationId;
    private final int secondStationId;

    public DeleteRailwayTrackCommand(
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
