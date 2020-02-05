package com.ralfhenze.railplan.application.commands;

public class AddRailwayTrackByStationIdCommand implements Command {

    private final String networkId;
    private final int firstStationId;
    private final int secondStationId;

    public AddRailwayTrackByStationIdCommand(
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
