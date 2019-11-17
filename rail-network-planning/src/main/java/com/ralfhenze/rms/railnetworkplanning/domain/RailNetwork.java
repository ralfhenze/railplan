package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.*;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;
import org.eclipse.collections.api.set.ImmutableSet;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan is a single graph without unconnected sub-graphs
 * [x] all invariants of RailNetworkDraft
 * [ ] released Rail Network Plans can't be changed any more
 */
class RailNetwork implements Aggregate {

    private final RailNetworkId id;
    private final RailNetworkPeriod period;

    private final ImmutableSet<TrainStation> stations;
    private final ImmutableSet<DoubleTrackRailway> connections;
    private final ImmutableSet<Invariant> invariants;

    RailNetwork(
        final RailNetworkId id,
        final RailNetworkPeriod period,
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<DoubleTrackRailway> connections
    ) {
        this.id = ensureNotNull(id, "Rail Network ID");
        this.period = ensureNotNull(period, "Rail Network Period");
        this.stations = ensureNotNull(stations, "Train Stations");
        this.connections = ensureNotNull(connections, "Connections");

        this.invariants = DefaultRailNetworkInvariants.INVARIANTS
            .newWith(new ContainsAtLeastTwoStationsAndOneTrack())
            .newWith(new ContainsNoUnconnectedSubGraphs());

        ensureInvariants();
    }

    private void ensureInvariants() {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations.castToSet(), connections.castToSet());
        }
    }

    public RailNetworkPeriod getPeriod() {
        return period;
    }

    public ImmutableSet<DoubleTrackRailway> getConnections() {
        return connections;
    }
}
