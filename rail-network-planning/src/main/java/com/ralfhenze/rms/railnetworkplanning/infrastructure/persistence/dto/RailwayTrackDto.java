package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;

public class RailwayTrackDto {

    private int firstStationId;
    private int secondStationId;

    public RailwayTrackDto() {}

    public RailwayTrackDto(RailwayTrack track) {
        this.firstStationId = Integer.parseInt(track.getFirstStationId().toString());
        this.secondStationId = Integer.parseInt(track.getSecondStationId().toString());
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
