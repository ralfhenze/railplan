package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a TrainStation from a RailNetworkDraft.
 */
public class DeleteTrainStationCommand implements Command {

    private final String draftId;
    private final int stationId;

    public DeleteTrainStationCommand(final String draftId, final int stationId) {
        this.draftId = draftId;
        this.stationId = stationId;
    }

    public String getDraftId() {
        return draftId;
    }

    public int getStationId() {
        return stationId;
    }
}
