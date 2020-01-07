package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to update an existing TrainStation of a RailNetworkDraft.
 */
public class UpdateTrainStationCommand implements Command {

    private final String draftId;
    private final String stationId;
    private final String stationName;
    private final double latitude;
    private final double longitude;

    public UpdateTrainStationCommand(
        final String draftId,
        final String stationId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        this.draftId = draftId;
        this.stationId = stationId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDraftId() {
        return draftId;
    }

    public String getStationId() {
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
