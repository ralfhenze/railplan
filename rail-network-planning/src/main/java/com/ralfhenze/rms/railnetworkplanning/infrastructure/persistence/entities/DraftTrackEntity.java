package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DraftTracks")
public class DraftTrackEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "stationId1")
    private DraftStationEntity firstStation;

    @ManyToOne
    @JoinColumn(name = "stationId2")
    private DraftStationEntity secondStation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DraftStationEntity getFirstStation() {
        return firstStation;
    }

    public void setFirstStation(DraftStationEntity firstStation) {
        this.firstStation = firstStation;
    }

    public DraftStationEntity getSecondStation() {
        return secondStation;
    }

    public void setSecondStation(DraftStationEntity secondStation) {
        this.secondStation = secondStation;
    }
}
