package com.ralfhenze.railplan.application.commands;

public class AddRailwayTrackByStationNameCommand implements Command {

    private final String networkId;
    private final String firstStationName;
    private final String secondStationName;

    public AddRailwayTrackByStationNameCommand(
        final String networkId,
        final String firstStationName,
        final String secondStationName
    ) {
        this.networkId = networkId;
        this.firstStationName = firstStationName;
        this.secondStationName = secondStationName;
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getFirstStationName() {
        return firstStationName;
    }

    public String getSecondStationName() {
        return secondStationName;
    }
}
