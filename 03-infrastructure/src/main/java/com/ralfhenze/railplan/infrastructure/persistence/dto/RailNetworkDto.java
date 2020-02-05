package com.ralfhenze.railplan.infrastructure.persistence.dto;

import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import org.springframework.data.annotation.Id;

import java.util.List;

public class RailNetworkDto {

    @Id
    private String id;
    private List<TrainStationDto> stations;
    private List<RailwayTrackDto> tracks;

    public RailNetworkDto() {}

    public RailNetworkDto(final RailNetwork network) {
        if (network.getId().isPresent()) {
            this.id = network.getId().get().toString();
        }
        this.stations = network.getStations().collect(TrainStationDto::new).castToList();
        this.tracks = network.getTracks().collect(RailwayTrackDto::new).castToList();
    }

    public RailNetwork toRailNetwork() {
        var network = new RailNetwork()
            .withId(new RailNetworkId(String.valueOf(id)));

        for (final var station : stations) {
            network = network.addStation(
                station.getId(),
                station.getName(),
                station.getLatitude(),
                station.getLongitude()
            );
        }

        for (final var track : tracks) {
            network = network.addTrackBetween(
                track.getFirstStationId(),
                track.getSecondStationId()
            );
        }

        return network;
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

    public void setStations(final List<TrainStationDto> stations) {
        this.stations = stations;
    }

    public List<RailwayTrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(final List<RailwayTrackDto> tracks) {
        this.tracks = tracks;
    }
}
