package com.ralfhenze.railplan.infrastructure.persistence.dto;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;

public class TrainStationDto {

    private int id;
    private String name;
    private double latitude;
    private double longitude;

    public TrainStationDto() {}

    public TrainStationDto(TrainStation station) {
        this.id = Integer.parseInt(station.getId().toString());
        this.name = station.getName().getName();
        this.latitude = station.getLocation().getLatitude();
        this.longitude = station.getLocation().getLongitude();
    }

    public TrainStation toTrainStation() {
        return new TrainStation(
            new TrainStationId(id),
            new TrainStationName(name),
            new GeoLocationInGermany(latitude, longitude)
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
