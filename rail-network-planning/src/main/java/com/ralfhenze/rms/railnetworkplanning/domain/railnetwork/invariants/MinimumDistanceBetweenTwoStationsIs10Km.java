package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Combinations;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;
import org.javatuples.Pair;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class MinimumDistanceBetweenTwoStationsIs10Km implements Invariant {

    private final static int MINIMUM_STATION_DISTANCE_KM = 10;

    private final Combinations combinations = new Combinations();

    @Override
    public void ensureIsSatisfied(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        ensureNotNull(stations, "Train Stations");

        if (stations.size() >= 2) {
            ensureMinimumStationDistance(stations);
        }
    }

    private void ensureMinimumStationDistance(final ImmutableList<TrainStation> stations) {

        // When have 4 stations A, B, C, D, we only need to calculate the distance
        // of unique station combinations (sourceStation, otherStation):
        // (A,B), (A,C), (A,D),
        // (B,C), (B,D),
        // (C,D)
        // because distance(A,B) = distance(B,A)

        final Optional<Pair<TrainStation, TrainStation>> tooNearStationCombination = combinations
            .getUniqueCombinations(stations)
            .detectOptional(stationCombination ->
                stationCombination
                    .getValue0()
                    .getLocation()
                    .getKilometerDistanceTo(
                        stationCombination
                            .getValue1()
                            .getLocation()
                    ) <= MINIMUM_STATION_DISTANCE_KM
            );

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
