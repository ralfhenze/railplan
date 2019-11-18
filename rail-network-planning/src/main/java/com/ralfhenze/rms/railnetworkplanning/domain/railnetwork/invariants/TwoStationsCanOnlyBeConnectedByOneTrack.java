package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class TwoStationsCanOnlyBeConnectedByOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<RailwayTrack> connections) {
        ensureNotNull(stations, "Stations");
        ensureNotNull(connections, "Connections");

        if (connections.size() >= 2) {
            ensureNoDuplicateConnections(stations, connections);
        }
    }

    private void ensureNoDuplicateConnections(Set<TrainStation> stations, Set<RailwayTrack> connections) {

        final List<Pair<RailwayTrack, RailwayTrack>> uniqueConnectionCombinations = new ArrayList<>();
        final Deque<RailwayTrack> sourceConnections = new LinkedList<>(connections);
        final Deque<RailwayTrack> otherConnections = new LinkedList<>(connections);

        sourceConnections.removeLast();

        for (final RailwayTrack sourceConnection : sourceConnections) {
            otherConnections.removeFirst();
            for (RailwayTrack otherConnection : otherConnections) {
                uniqueConnectionCombinations.add(new Pair<>(sourceConnection, otherConnection));
            }
        }

        final Optional<Pair<RailwayTrack, RailwayTrack>> equalConnectionCombination = uniqueConnectionCombinations
            .stream()
            .filter(stationCombination ->
                stationCombination
                    .getValue0()
                    .equals(stationCombination.getValue1())
                )
            .findAny();

        if (equalConnectionCombination.isPresent()) {
            final TrainStationId id1 = equalConnectionCombination.get().getValue0().getFirstStationId();
            final TrainStationId id2 = equalConnectionCombination.get().getValue0().getSecondStationId();
            final Map<TrainStationId, TrainStation> stationsMap = stations.stream()
                .collect(Collectors.toMap(TrainStation::getId, ts -> ts));

            throw new IllegalArgumentException(
                "Connection from \""
                    + stationsMap.get(id1).getName() + "\" to \""
                    + stationsMap.get(id2).getName() + "\" already exists"
            );
        }
    }
}