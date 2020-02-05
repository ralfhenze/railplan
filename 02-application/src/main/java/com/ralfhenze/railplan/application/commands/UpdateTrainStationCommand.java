package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to update an existing TrainStation of a RailNetwork.
 */
public class UpdateTrainStationCommand implements Command {

    private final String networkId;
    private final int stationId;
    private final String stationName;
    private final double latitude;
    private final double longitude;

    public UpdateTrainStationCommand(
        final String networkId,
        final int stationId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        this.networkId = networkId;
        this.stationId = stationId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNetworkId() {
        return networkId;
    }

    public int getStationId() {
        return stationId;
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
