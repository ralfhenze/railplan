package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to add a new TrainStation to a RailNetworkDraft
 */
public class AddTrainStationCommand implements Command {

    private final String draftId;
    private final String stationName;
    private final double latitude;
    private final double longitude;

    public AddTrainStationCommand(
        final String draftId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        this.draftId = draftId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDraftId() {
        return draftId;
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
