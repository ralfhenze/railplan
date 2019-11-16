package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.*;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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

    private final Set<TrainStation> stations;
    private final Set<DoubleTrackRailway> connections;
    private final Set<Invariant> invariants = new LinkedHashSet<>(Arrays.asList(
        new MinimumDistanceBetweenTwoStationsIs10Km(),
        new TwoStationsCanOnlyBeConnectedByOneTrack(),
        new MaximumLengthOfTrackIs300Km(),
        new StationNamesAreUnique(),

        new ContainsAtLeastTwoStationsAndOneTrack(),
        new ContainsNoUnconnectedSubGraphs()
    ));

    RailNetwork(
        final RailNetworkId id,
        final RailNetworkPeriod period,
        final Set<TrainStation> stations,
        final Set<DoubleTrackRailway> connections
    ) {
        this.id = ensureNotNull(id, "Rail Network ID");
        this.period = ensureNotNull(period, "Rail Network Period");
        this.stations = ensureNotNull(stations, "Train Stations");
        this.connections = ensureNotNull(connections, "Connections");

        ensureInvariants();
    }

    private void ensureInvariants() {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations, connections);
        }
    }
}
