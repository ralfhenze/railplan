package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.entities;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Drafts")
public class DraftEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @OneToMany
    @JoinColumn(name = "draftId")
    private Set<DraftStationEntity> stations;

    @OneToMany
    @JoinColumn(name = "draftId")
    private Set<DraftTrackEntity> tracks;

    public RailNetworkDraft toRailNetworkDraft() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withId(new RailNetworkDraftId(String.valueOf(id)));

        for (DraftStationEntity station : stations) {
            draft = draft.withNewStation(
                new TrainStationName(station.getName()),
                new GeoLocationInGermany(station.getLatitude(), station.getLongitude())
            );
        }

        return draft;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Set<DraftStationEntity> getStations() {
        return stations;
    }

    public void setStations(Set<DraftStationEntity> stations) {
        this.stations = stations;
    }

    public Set<DraftTrackEntity> getTracks() {
        return tracks;
    }

    public void setTracks(Set<DraftTrackEntity> tracks) {
        this.tracks = tracks;
    }
}
