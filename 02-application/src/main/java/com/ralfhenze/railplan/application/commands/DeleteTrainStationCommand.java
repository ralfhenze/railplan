package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a TrainStation from a RailNetworkDraft.
 */
public class DeleteTrainStationCommand implements Command {

    private final String draftId;
    private final String stationId;

    public DeleteTrainStationCommand(final String draftId, final String stationId) {
        this.draftId = draftId;
        this.stationId = stationId;
    }

    public String getDraftId() {
        return draftId;
    }

    public String getStationId() {
        return stationId;
    }
}
