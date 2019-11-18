package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

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

        if (!stations.isEmpty() && !connections.isEmpty()) {
            ensureNoUnconnectedSubGraphs(stations, connections);
        }
    }

    private void ensureNoUnconnectedSubGraphs(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        final Map<TrainStationId, Set<TrainStationId>> nodes = new HashMap<>();

        for (final DoubleTrackRailway connection : connections) {
            final TrainStationId id1 = connection.getFirstStationId();
            final TrainStationId id2 = connection.getSecondStationId();

            final Set<TrainStationId> set1 = nodes.getOrDefault(id1, new HashSet<>());
            set1.add(id2);
            nodes.put(id1, set1);

            final Set<TrainStationId> set2 = nodes.getOrDefault(id2, new HashSet<>());
            set2.add(id1);
            nodes.put(id2, set2);
        }

        final TrainStationId firstNode = nodes.keySet().stream().findFirst().get();
        final Set<TrainStationId> visitedNodes = visitAdjacentNodes(firstNode, new HashSet<>(), nodes);

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

    private Set<TrainStationId> visitAdjacentNodes(
        final TrainStationId node,
        final Set<TrainStationId> visitedNodes,
        final Map<TrainStationId, Set<TrainStationId>> nodes
    ) {
        visitedNodes.add(node);

        final Set<TrainStationId> unvisitedAdjacentNodes = new HashSet<>(nodes.get(node));
        unvisitedAdjacentNodes.removeAll(visitedNodes);

        for (final TrainStationId adjacentNode : unvisitedAdjacentNodes) {
            visitedNodes.addAll(
                visitAdjacentNodes(adjacentNode, new HashSet<>(visitedNodes), nodes)
            );
        }

        return visitedNodes;
    }
}
