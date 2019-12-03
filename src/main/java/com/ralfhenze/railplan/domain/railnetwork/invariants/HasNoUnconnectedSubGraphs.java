package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.*;

public class HasNoUnconnectedSubGraphs
    implements ValidationConstraint<ImmutableList<RailwayTrack>> {

    private final ImmutableList<TrainStation> stations;

    public HasNoUnconnectedSubGraphs(final ImmutableList<TrainStation> stations) {
        this.stations = stations;
    }

    @Override
    public Optional<ErrorMessage> validate(
        final ImmutableList<RailwayTrack> tracks,
        final String fieldName
    ) {
        if (stations.isEmpty() || tracks.isEmpty()) {
            return Optional.empty();
        }

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
        final Set<TrainStationId> visitedNodes = visitAdjacentNodes(
            firstNode, new HashSet<>(), nodes
        );

        for (final var station : stations) {
            nodes.putIfAbsent(station.getId(), new HashSet<>());
        }

        final var unconnectedSubGraphExists = (visitedNodes.size() < nodes.size());

        if (unconnectedSubGraphExists) {
            return Optional.of(new ErrorMessage(
                "Unconnected sub-graphs are not allowed! Please make sure that the rail network"
                    + " is a single graph and all stations are reachable from each other."
            ));
        }

        return Optional.empty();
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
