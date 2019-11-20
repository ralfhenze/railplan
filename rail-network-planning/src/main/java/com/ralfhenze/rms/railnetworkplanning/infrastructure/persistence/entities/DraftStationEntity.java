package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.entities;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DraftStations")
public class DraftStationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private double latitude;
    private double longitude;

    public DraftStationEntity() {}

    public DraftStationEntity(TrainStation station) {
        this.name = station.getName().getName();
        this.latitude = station.getLocation().getLatitude();
        this.longitude = station.getLocation().getLongitude();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
