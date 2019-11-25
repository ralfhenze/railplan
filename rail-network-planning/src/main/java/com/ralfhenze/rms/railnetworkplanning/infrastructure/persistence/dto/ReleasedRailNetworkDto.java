package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ValidityPeriod;
import org.eclipse.collections.api.factory.Sets;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class ReleasedRailNetworkDto {

    @Id
    private String id;
    private String startDate;
    private String endDate;
    private Set<TrainStationDto> stations;
    private Set<RailwayTrackDto> tracks;

    public ReleasedRailNetworkDto() {}

    public ReleasedRailNetworkDto(ReleasedRailNetwork network) {
        this.startDate = network.getPeriod().getStartDate().toString();
        this.endDate = network.getPeriod().getEndDate().toString();
        this.stations = network.getStations().collect(TrainStationDto::new).castToSet();
        this.tracks = network.getTracks().collect(RailwayTrackDto::new).castToSet();
    }

    public ReleasedRailNetwork toReleasedRailNetwork() {
        return new ReleasedRailNetwork(
            Optional.of(new ReleasedRailNetworkId(String.valueOf(id))),
            new ValidityPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate)),
            Sets.immutable.ofAll(stations).collect(TrainStationDto::toTrainStation),
            Sets.immutable.ofAll(tracks).collect(RailwayTrackDto::toRailwayTrack)
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
