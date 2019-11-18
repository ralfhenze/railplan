package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class MaximumLengthOfTrackIs300Km implements Invariant {

    private static final int MAXIMUM_LENGTH_KM = 300;

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        ensureNotNull(stations, "Stations");
        ensureNotNull(connections, "Connections");

        if (!connections.isEmpty()) {
            ensureMaximumTrackLength(stations, connections);
        }
    }

    private void ensureMaximumTrackLength(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        final Map<StationId, TrainStation> stationsMap = stations.stream()
            .collect(Collectors.toMap(TrainStation::getId, ts -> ts));

        final Map<DoubleTrackRailway, Double> trackLengthPerConnection = connections.stream()
            .collect(Collectors.toMap(
                c -> c,
                c -> stationsMap
                    .get(c.getFirstStationId())
                    .getLocation()
                    .getLocation()
                    .getKilometerDistanceTo(
                        stationsMap
                            .get(c.getSecondStationId())
                            .getLocation()
                            .getLocation()
                    )
                )
            );

        Optional<Entry<DoubleTrackRailway, Double>> tooLongTrack = trackLengthPerConnection
            .entrySet()
            .stream()
            .filter(c -> c.getValue() > MAXIMUM_LENGTH_KM)
            .findAny();

        if (tooLongTrack.isPresent()) {
            final StationId id1 = tooLongTrack.get().getKey().getFirstStationId();
            final StationId id2 = tooLongTrack.get().getKey().getSecondStationId();
            final double trackLength = tooLongTrack.get().getValue();

            throw new IllegalArgumentException(
                "Track from \""
                    + stationsMap.get(id1).getName() + "\" (" + id1 + ") to \""
                    + stationsMap.get(id2).getName() + "\" (" + id2 + ") must be shorter than "
                    + MAXIMUM_LENGTH_KM + " km"
                    + ", but was ~" + Math.round(trackLength) + " km"
            );
        }
    }
}
