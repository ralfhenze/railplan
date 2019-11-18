package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants.ContainsAtLeastTwoStationsAndOneTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants.ContainsNoUnconnectedSubGraphs;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants.DefaultRailNetworkInvariants;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants.Invariant;
import org.eclipse.collections.api.set.ImmutableSet;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan is a single graph without unconnected sub-graphs
 * [x] all invariants of RailNetworkDraft
 * [ ] released Rail Network Plans can't be changed any more
 */
public class ReleasedRailNetwork implements Aggregate {

    private final Optional<ReleasedRailNetworkId> id;
    private final ValidityPeriod period;

    private final ImmutableSet<TrainStation> stations;
    private final ImmutableSet<RailwayTrack> tracks;
    private final ImmutableSet<Invariant> invariants;

    public ReleasedRailNetwork(
        final ValidityPeriod period,
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<RailwayTrack> tracks
    ) {
        this(Optional.empty(), period, stations, tracks);
    }

    public ReleasedRailNetwork(
        final Optional<ReleasedRailNetworkId> id,
        final ValidityPeriod period,
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<RailwayTrack> tracks
    ) {
        this.id = ensureNotNull(id, "Rail Network ID");
        this.period = ensureNotNull(period, "Validity Period");
        this.stations = ensureNotNull(stations, "Train Stations");
        this.tracks = ensureNotNull(tracks, "Railway Tracks");

        this.invariants = DefaultRailNetworkInvariants.INVARIANTS
            .newWith(new ContainsAtLeastTwoStationsAndOneTrack())
            .newWith(new ContainsNoUnconnectedSubGraphs());

        ensureInvariants();
    }

    private void ensureInvariants() {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations, tracks);
        }
    }

    public ReleasedRailNetwork withId(ReleasedRailNetworkId id) {
        ensureNotNull(id, "Rail Network ID");

        return new ReleasedRailNetwork(Optional.of(id), period, stations, tracks);
    }

    public Optional<ReleasedRailNetworkId> getId() {
        return id;
    }

    public ValidityPeriod getPeriod() {
        return period;
    }

    public ImmutableSet<RailwayTrack> getTracks() {
        return tracks;
    }
}
