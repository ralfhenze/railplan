package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import org.springframework.data.annotation.Id;

import java.util.Set;

public class RailNetworkDraftDto {

    @Id
    private String id;
    private Set<TrainStationDto> stations;
    private Set<RailwayTrackDto> tracks;

    public RailNetworkDraftDto() {}

    public RailNetworkDraftDto(RailNetworkDraft draft) {
        this.stations = draft.getStations().collect(TrainStationDto::new).castToSet();
        this.tracks = draft.getTracks().collect(RailwayTrackDto::new).castToSet();
    }

    public RailNetworkDraft toRailNetworkDraft() {
        RailNetworkDraft draft = new RailNetworkDraft()
            .withId(new RailNetworkDraftId(String.valueOf(id)));

        for (TrainStationDto station : stations) {
            draft = draft.withNewStation(
                new TrainStationName(station.getName()),
                new GeoLocationInGermany(station.getLatitude(), station.getLongitude())
            );
        }

        for (RailwayTrackDto track : tracks) {
            draft = draft.withNewTrack(
                new TrainStationId(String.valueOf(track.getFirstStationId())),
                new TrainStationId(String.valueOf(track.getSecondStationId()))
            );
        }

        return draft;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<TrainStationDto> getStations() {
        return stations;
    }

    public void setStations(Set<TrainStationDto> stations) {
        this.stations = stations;
    }

    public Set<RailwayTrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(Set<RailwayTrackDto> tracks) {
        this.tracks = tracks;
    }
}
