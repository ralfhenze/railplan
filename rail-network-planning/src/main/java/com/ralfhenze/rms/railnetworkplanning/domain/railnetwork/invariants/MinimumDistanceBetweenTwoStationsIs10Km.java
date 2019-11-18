package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.javatuples.Pair;

import java.util.*;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class MinimumDistanceBetweenTwoStationsIs10Km implements Invariant {

    private final static int MINIMUM_STATION_DISTANCE_KM = 10;

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<RailwayTrack> connections) {
        ensureNotNull(stations, "Stations");

        if (stations.size() >= 2) {
            ensureMinimumStationDistance(stations);
        }
    }

    private void ensureMinimumStationDistance(Set<TrainStation> stations) {

        // When have 4 stations A, B, C, D, we only need to calculate the distance
        // of unique station combinations (sourceStation, otherStation):
        // (A,B), (A,C), (A,D),
        // (B,C), (B,D),
        // (C,D)
        // because distance(A,B) = distance(B,A)

        final List<Pair<TrainStation, TrainStation>> uniqueStationCombinations = new ArrayList<>();
        final Deque<TrainStation> sourceStations = new LinkedList<>(stations);
        final Deque<TrainStation> otherStations = new LinkedList<>(stations);

        sourceStations.removeLast();

        for (final TrainStation sourceStation : sourceStations) {
            otherStations.removeFirst();
            for (TrainStation otherStation : otherStations) {
                uniqueStationCombinations.add(new Pair<>(sourceStation, otherStation));
            }
        }

        final Optional<Pair<TrainStation, TrainStation>> tooNearStationCombination = uniqueStationCombinations
            .stream()
            .filter(stationCombination ->
                stationCombination
                    .getValue0()
                    .getLocation()
                    .getKilometerDistanceTo(
                        stationCombination
                            .getValue1()
                            .getLocation()
                    ) <= MINIMUM_STATION_DISTANCE_KM)
            .findAny();

        if (tooNearStationCombination.isPresent()) {
            final TrainStation firstStation = tooNearStationCombination.get().getValue0();
            final TrainStation secondStation = tooNearStationCombination.get().getValue1();

            final double distance = firstStation.getLocation()
                .getKilometerDistanceTo(secondStation.getLocation());

            throw new IllegalArgumentException(
                "Distance between Station \""
                    + firstStation.getName() + "\" and \"" + secondStation.getName()
                    + "\" should be > 10 km, but was ~"
                    + (Math.round(distance * 100.0) / 100.0)
                    + " km"
            );
        }
    }
}
