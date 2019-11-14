package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.ContainsAtLeastTwoStationsAndOneTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.ContainsNoUnconnectedSubGraphs;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.*;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [ ] the Rail Network Plan contains no stand-alone / unconnected Stations
 * [ ] the Rail Network Plan is a single graph without unconnected islands / sub-graphs
 * [ ] released Rail Network Plans can't be changed any more
 * [ ] all invariants of RailNetworkDraft
 */
class RailNetwork implements Aggregate {

    private final RailNetworkId id;
    private final RailNetworkPeriod period;
    private final RailNetworkGraph graph = new RailNetworkGraph(new HashSet<>(Arrays.asList(
        new ContainsAtLeastTwoStationsAndOneTrack(),
        new ContainsNoUnconnectedSubGraphs()
    )));

    private final Map<StationId, TrainStation> stations;
    private final Set<DoubleTrackRailway> connections;

    RailNetwork(
        final RailNetworkId id,
        final RailNetworkPeriod period,
        final Set<TrainStation> stations,
        final Set<DoubleTrackRailway> connections
    ) {
        this.id = ensureNotNull(id, "Rail Network ID");
        this.period = ensureNotNull(period, "Rail Network Period");
        this.stations = ensureNotNull(stations, "Train Stations")
            .stream().collect(Collectors.toMap(TrainStation::getId, t -> t));
        this.connections = ensureNotNull(connections, "Connections");

        ensureAtLeastTwoStations(); // TODO: this is eventually not needed, because one track implies two stations
        ensureAtLeastOneTrack();
    }

    private void ensureAtLeastTwoStations() {
        if (stations.size() < 2) {
            throw new IllegalArgumentException(
                "At least 2 stations are required, but got " + stations.size()
            );
        }
    }

    private void ensureAtLeastOneTrack() {
        if (connections.size() < 1) {
            throw new IllegalArgumentException(
                "At least 1 connection is required, but got " + connections.size()
            );
        }
    }
}
