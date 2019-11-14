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
 * [x] the Rail Network Plan is a single graph without unconnected sub-graphs
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
        ensureNoUnconnectedSubGraphs();
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

    private void ensureNoUnconnectedSubGraphs() {
        final Map<StationId, Set<StationId>> nodes = new HashMap<>();

        for (final DoubleTrackRailway connection : connections) {
            final StationId id1 = connection.getFirstStationId();
            final StationId id2 = connection.getSecondStationId();

            final Set<StationId> set1 = nodes.getOrDefault(id1, new HashSet<>());
            set1.add(id2);
            nodes.put(id1, set1);

            final Set<StationId> set2 = nodes.getOrDefault(id2, new HashSet<>());
            set2.add(id1);
            nodes.put(id2, set2);
        }

        final StationId firstNode = nodes.keySet().stream().findFirst().get();
        final Set<StationId> visitedNodes = visitAdjacentNodes(firstNode, new HashSet<>(), nodes);

        final boolean unconnectedSubGraphExists = (visitedNodes.size() < nodes.size());

        if (unconnectedSubGraphExists) {
            throw new IllegalArgumentException(
                "Unconnected sub-graphs are not allowed! Please make sure that the rail network"
                    + " is a single graph and all stations are reachable from each other."
            );
        }
    }

    private Set<StationId> visitAdjacentNodes(
        final StationId node,
        final Set<StationId> visitedNodes,
        final Map<StationId, Set<StationId>> nodes
    ) {
        visitedNodes.add(node);

        final Set<StationId> unvisitedAdjacentNodes = new HashSet<>(nodes.get(node));
        unvisitedAdjacentNodes.removeAll(visitedNodes);

        for (final StationId adjacentNode : unvisitedAdjacentNodes) {
            visitedNodes.addAll(
                visitAdjacentNodes(adjacentNode, new HashSet<>(visitedNodes), nodes)
            );
        }

        return visitedNodes;
    }
}
