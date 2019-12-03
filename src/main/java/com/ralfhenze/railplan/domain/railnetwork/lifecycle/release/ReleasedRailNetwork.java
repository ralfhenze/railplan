package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Aggregate;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.HasMinSize;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotNull;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.invariants.*;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

/**
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan is a single graph without unconnected sub-graphs
 * [x] all invariants of RailNetworkDraft
 * [ ] released Rail Network Plans can't be changed any more
 */
public class ReleasedRailNetwork implements Aggregate {

    private final Optional<ReleasedRailNetworkId> id;
    private final ValidityPeriod period;
    private final ImmutableList<TrainStation> stations;
    private final ImmutableList<RailwayTrack> tracks;

    public ReleasedRailNetwork(
        final ValidityPeriod period,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) throws ValidationException {
        this(Optional.empty(), period, stations, tracks);
    }

    public ReleasedRailNetwork(
        final Optional<ReleasedRailNetworkId> id,
        final ValidityPeriod period,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) throws ValidationException {
        new Validation()
            .ensureThat(id, new IsNotNull(), "Rail Network ID")
            .ensureThat(period, new IsNotNull(), "Validity Period")
            .ensureThat(stations, new HasMinSize(2), "Train Stations")
            .ensureThat(stations, new HasNoStationsNearerThan10Km(), "Train Stations")
            .ensureThat(stations, new HasUniqueStationNames(), "Station Name")
            .ensureThat(tracks, new HasMinSize(1), "Railway Tracks")
            .ensureThat(tracks, new HasNoTracksLongerThan300Km(stations), "Railway Tracks")
            .ensureThat(tracks, new HasNoDuplicateTracks(stations), "Railway Tracks")
            .ensureThat(tracks, new HasNoUnconnectedSubGraphs(stations), "Railway Tracks")
            .throwExceptionIfInvalid();

        this.id = id;
        this.period = period;
        this.stations = stations;
        this.tracks = tracks;
    }

    public Optional<ReleasedRailNetworkId> getId() {
        return id;
    }

    public ValidityPeriod getPeriod() {
        return period;
    }

    public ImmutableList<TrainStation> getStations() {
        return stations;
    }

    public ImmutableList<RailwayTrack> getTracks() {
        return tracks;
    }
}
