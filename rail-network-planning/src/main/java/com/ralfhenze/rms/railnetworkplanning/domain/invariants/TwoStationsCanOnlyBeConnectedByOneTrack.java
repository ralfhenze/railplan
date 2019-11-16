package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class TwoStationsCanOnlyBeConnectedByOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        ensureNotNull(stations, "Stations");
        ensureNotNull(connections, "Connections");

        if (connections.size() >= 2) {
            ensureNoDuplicateConnections(stations, connections);
        }
    }

    private void ensureNoDuplicateConnections(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {

        final List<Pair<DoubleTrackRailway, DoubleTrackRailway>> uniqueConnectionCombinations = new ArrayList<>();
        final Deque<DoubleTrackRailway> sourceConnections = new LinkedList<>(connections);
        final Deque<DoubleTrackRailway> otherConnections = new LinkedList<>(connections);

        sourceConnections.removeLast();

        for (final DoubleTrackRailway sourceConnection : sourceConnections) {
            otherConnections.removeFirst();
            for (DoubleTrackRailway otherConnection : otherConnections) {
                uniqueConnectionCombinations.add(new Pair<>(sourceConnection, otherConnection));
            }
        }

        final Optional<Pair<DoubleTrackRailway, DoubleTrackRailway>> equalConnectionCombination = uniqueConnectionCombinations
            .stream()
            .filter(stationCombination ->
                stationCombination
                    .getValue0()
                    .equals(stationCombination.getValue1())
                )
            .findAny();

        if (equalConnectionCombination.isPresent()) {
            final StationId id1 = equalConnectionCombination.get().getValue0().getFirstStationId();
            final StationId id2 = equalConnectionCombination.get().getValue0().getSecondStationId();
            final Map<StationId, TrainStation> stationsMap = stations.stream()
                .collect(Collectors.toMap(TrainStation::getId, ts -> ts));

            throw new IllegalArgumentException(
                "Connection from \""
                    + stationsMap.get(id1).getName() + "\" to \""
                    + stationsMap.get(id2).getName() + "\" already exists"
            );
        }
    }
}
