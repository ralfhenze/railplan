package com.ralfhenze.railplan.infrastructure.persistence.dto;

import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import org.springframework.data.annotation.Id;

import java.util.List;

public class RailNetworkDraftDto {

    @Id
    private String id;
    private List<TrainStationDto> stations;
    private List<RailwayTrackDto> tracks;

    public RailNetworkDraftDto() {}

    public RailNetworkDraftDto(RailNetworkDraft draft) {
        if (draft.getId().isPresent()) {
            this.id = draft.getId().get().toString();
        }
        this.stations = draft.getStations().collect(TrainStationDto::new).castToList();
        this.tracks = draft.getTracks().collect(RailwayTrackDto::new).castToList();
    }

    public RailNetworkDraft toRailNetworkDraft() {
        var draft = new RailNetworkDraft()
            .withId(new RailNetworkDraftId(String.valueOf(id)));

        for (final var station : stations) {
            draft = draft.withNewStation(
                station.getName(),
                station.getLatitude(),
                station.getLongitude()
            );
        }

        for (final var track : tracks) {
            draft = draft.withNewTrack(
                new TrainStationId(track.getFirstStationId()),
                new TrainStationId(track.getSecondStationId())
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

    public List<TrainStationDto> getStations() {
        return stations;
    }

    public void setStations(List<TrainStationDto> stations) {
        this.stations = stations;
    }

    public List<RailwayTrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<RailwayTrackDto> tracks) {
        this.tracks = tracks;
    }
}
