package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

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