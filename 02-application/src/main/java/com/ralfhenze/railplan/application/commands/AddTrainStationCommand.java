package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to add a new TrainStation to a RailNetwork.
 */
public class AddTrainStationCommand implements Command {

    private final String networkId;
    private final String stationName;
    private final double latitude;
    private final double longitude;

    public AddTrainStationCommand(
        final String networkId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        this.networkId = networkId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getStationName() {
        return stationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
