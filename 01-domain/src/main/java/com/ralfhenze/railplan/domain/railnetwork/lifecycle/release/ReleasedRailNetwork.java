package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Aggregate;
import com.ralfhenze.railplan.domain.common.Validatable;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.constraints.HasMinSize;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoDuplicateTracks;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoStationsNearerThan10Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoTracksLongerThan300Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoUnconnectedSubGraphs;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasUniqueStationNames;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.List;
import java.util.Optional;

/**
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan is a single graph without unconnected sub-graphs
 * [x] all invariants of RailNetworkDraft
 * [ ] released Rail Network Plans can't be changed any more
 */
public class ReleasedRailNetwork implements Aggregate, Validatable {

    private final Optional<ReleasedRailNetworkId> id;
    private final ValidityPeriod period;
    private final ImmutableList<TrainStation> stations;
    private final ImmutableList<RailwayTrack> tracks;

    public ReleasedRailNetwork(
        final ValidityPeriod period,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        this(Optional.empty(), period, stations, tracks);
    }

    public ReleasedRailNetwork(
        final Optional<ReleasedRailNetworkId> id,
        final ValidityPeriod period,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        this.id = id;
        this.period = period;
        this.stations = stations;
        this.tracks = tracks;
    }

    @Override
    public boolean isValid() {
        return id.map(ReleasedRailNetworkId::isValid).orElse(true)
            && period.isValid()
            && stations.allSatisfy(TrainStation::isValid)
            && tracks.allSatisfy(RailwayTrack::isValid)
            && getStationErrors().isEmpty()
            && getTrackErrors().isEmpty();
    }

    public List<ValidationError> getStationErrors() {
        return new Validation<>(stations)
            .ensureIt(new HasMinSize<>(2))
            .ensureIt(new HasUniqueStationNames())
            .ensureIt(new HasNoStationsNearerThan10Km())
            .getValidationErrors();
    }

    public List<ValidationError> getTrackErrors() {
        return new Validation<>(tracks)
            .ensureIt(new HasMinSize<>(1))
            .ensureIt(new HasNoTracksLongerThan300Km(stations))
            .ensureIt(new HasNoDuplicateTracks(stations))
            .ensureIt(new HasNoUnconnectedSubGraphs(stations))
            .getValidationErrors();
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
