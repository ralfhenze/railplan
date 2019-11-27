package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ValidityPeriod;
import org.eclipse.collections.api.factory.Lists;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReleasedRailNetworkDto {

    @Id
    private String id;
    private String startDate;
    private String endDate;
    private List<TrainStationDto> stations;
    private List<RailwayTrackDto> tracks;

    public ReleasedRailNetworkDto() {}

    public ReleasedRailNetworkDto(ReleasedRailNetwork network) {
        if (network.getId().isPresent()) {
            this.id = network.getId().get().toString();
        }
        this.startDate = network.getPeriod().getStartDate().toString();
        this.endDate = network.getPeriod().getEndDate().toString();
        this.stations = network.getStations().collect(TrainStationDto::new).castToList();
        this.tracks = network.getTracks().collect(RailwayTrackDto::new).castToList();
    }

    public ReleasedRailNetwork toReleasedRailNetwork() {
        return new ReleasedRailNetwork(
            Optional.of(new ReleasedRailNetworkId(String.valueOf(id))),
            new ValidityPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate)),
            Lists.immutable.ofAll(stations).collect(TrainStationDto::toTrainStation),
            Lists.immutable.ofAll(tracks).collect(RailwayTrackDto::toRailwayTrack)
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
