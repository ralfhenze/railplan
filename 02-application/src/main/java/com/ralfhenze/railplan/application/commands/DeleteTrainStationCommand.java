package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a TrainStation from a RailNetwork.
 */
public class DeleteTrainStationCommand implements Command {

    private final String networkId;
    private final int stationId;

    public DeleteTrainStationCommand(final String networkId, final int stationId) {
        this.networkId = networkId;
        this.stationId = stationId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public int getStationId() {
        return stationId;
    }
}
