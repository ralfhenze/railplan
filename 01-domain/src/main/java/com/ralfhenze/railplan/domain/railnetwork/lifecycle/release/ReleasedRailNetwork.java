package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Aggregate;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.HasMinSize;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoDuplicateTracks;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoStationsNearerThan10Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoTracksLongerThan300Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoUnconnectedSubGraphs;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasUniqueStationNames;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

/**
 * [x] the Released Rail Network contains at least two Stations and one Track
 * [x] the Released Rail Network is a single graph without unconnected sub-graphs
 * [x] all invariants of RailNetworkDraft
 * [ ] a Released Rail Network can't be changed any more
 */
public class ReleasedRailNetwork implements Aggregate {

    private final Optional<ReleasedRailNetworkId> id;
    private final ValidityPeriod period;
    private final ImmutableList<TrainStation> stations;
    private final ImmutableList<RailwayTrack> tracks;

    /**
     * Constructs a Network without an ID. Ensures that all Network invariants are satisfied.
     *
     * @throws ValidationException if any Network invariants are violated
     */
    public ReleasedRailNetwork(
        final ValidityPeriod period,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        this(Optional.empty(), period, stations, tracks);
    }

    /**
     * Constructs a Network. Ensures that all Network invariants are satisfied.
     *
     * @throws ValidationException if any Network invariants are violated
     */
    public ReleasedRailNetwork(
        final Optional<ReleasedRailNetworkId> id,
        final ValidityPeriod period,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        new Validation()
            .ensureThat(stations, new HasMinSize<>(2), Field.STATIONS)
            .ensureThat(stations, new HasUniqueStationNames(), Field.STATION_NAME)
            .ensureThat(stations, new HasNoStationsNearerThan10Km(), Field.LOCATION)
            .ensureThat(tracks, new HasMinSize<>(1), Field.TRACKS)
            .ensureThat(tracks, new HasNoTracksLongerThan300Km(stations), Field.TRACKS)
            .ensureThat(tracks, new HasNoDuplicateTracks(stations), Field.TRACKS)
            .ensureThat(tracks, new HasNoUnconnectedSubGraphs(stations), Field.TRACKS)
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
