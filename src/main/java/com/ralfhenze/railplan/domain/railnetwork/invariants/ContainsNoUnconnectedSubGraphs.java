package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

public class ContainsNoUnconnectedSubGraphs implements Invariant {

    @Override
    public void ensureIsSatisfied(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        ensureNotNull(stations, "Train Stations");
        ensureNotNull(tracks, "Railway Tracks");

        if (!stations.isEmpty() && !tracks.isEmpty()) {
            ensureNoUnconnectedSubGraphs(stations, tracks);
        }
    }

    private void ensureNoUnconnectedSubGraphs(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        final Map<TrainStationId, Set<TrainStationId>> nodes = new HashMap<>();

        for (final var track : tracks) {
            final var id1 = track.getFirstStationId();
            final var id2 = track.getSecondStationId();

            final Set<TrainStationId> set1 = nodes.getOrDefault(id1, new HashSet<>());
            set1.add(id2);
            nodes.put(id1, set1);

            final Set<TrainStationId> set2 = nodes.getOrDefault(id2, new HashSet<>());
            set2.add(id1);
            nodes.put(id2, set2);
        }

        final var firstNode = nodes.keySet().stream().findFirst().get();
        final Set<TrainStationId> visitedNodes = visitAdjacentNodes(firstNode, new HashSet<>(), nodes);

        for (final var station : stations) {
            nodes.putIfAbsent(station.getId(), new HashSet<>());
        }

        final var unconnectedSubGraphExists = (visitedNodes.size() < nodes.size());

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

        for (final var adjacentNode : unvisitedAdjacentNodes) {
            visitedNodes.addAll(
                visitAdjacentNodes(adjacentNode, new HashSet<>(visitedNodes), nodes)
            );
        }

        return visitedNodes;
    }
}
