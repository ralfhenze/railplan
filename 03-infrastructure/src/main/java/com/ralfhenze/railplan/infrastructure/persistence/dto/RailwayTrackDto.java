package com.ralfhenze.railplan.infrastructure.persistence.dto;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;

public class RailwayTrackDto {

    private int firstStationId;
    private int secondStationId;

    public RailwayTrackDto() {}

    public RailwayTrackDto(RailwayTrack track) {
        this.firstStationId = Integer.parseInt(track.getFirstStationId().toString());
        this.secondStationId = Integer.parseInt(track.getSecondStationId().toString());
    }

    public RailwayTrack toRailwayTrack() {
        return new RailwayTrack(
            new TrainStationId(firstStationId),
            new TrainStationId(secondStationId)
        );
    }

    public int getFirstStationId() {
        return firstStationId;
    }

    public void setFirstStationId(int firstStationId) {
        this.firstStationId = firstStationId;
    }

    public int getSecondStationId() {
        return secondStationId;
    }

    public void setSecondStationId(int secondStationId) {
        this.secondStationId = secondStationId;
    }
}
