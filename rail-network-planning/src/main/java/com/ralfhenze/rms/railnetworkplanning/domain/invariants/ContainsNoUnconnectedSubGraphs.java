package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class ContainsNoUnconnectedSubGraphs implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        ensureNotNull(stations, "Stations");
        ensureNotNull(connections, "Connections");

        ensureNoUnconnectedSubGraphs(stations, connections);
    }

    private void ensureNoUnconnectedSubGraphs(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
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

        for (final TrainStation station : stations) {
            nodes.putIfAbsent(station.getId(), new HashSet<>());
        }

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
