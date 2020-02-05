package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a RailwayTrack from a RailNetwork.
 */
public class DeleteRailwayTrackCommand implements Command {

    private final String networkId;
    private final int firstStationId;
    private final int secondStationId;

    public DeleteRailwayTrackCommand(
        final String networkId,
        final int firstStationId,
        final int secondStationId
    ) {
        this.networkId = networkId;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public int getFirstStationId() {
        return firstStationId;
    }

    public int getSecondStationId() {
        return secondStationId;
    }
}
