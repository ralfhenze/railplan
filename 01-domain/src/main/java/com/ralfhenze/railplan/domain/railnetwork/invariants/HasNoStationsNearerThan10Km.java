package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.Combinations;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

public class HasNoStationsNearerThan10Km
    implements ValidationConstraint<ImmutableList<TrainStation>> {

    private final static int MINIMUM_STATION_DISTANCE_KM = 10;

    private final Combinations combinations = new Combinations();

    @Override
    public Optional<ValidationError> validate(
        final ImmutableList<TrainStation> stations,
        final Field field
    ) {
        if (stations.size() < 2) {
            return Optional.empty();
        }

        // When have 4 stations A, B, C, D, we only need to calculate the distance
        // of unique station combinations (sourceStation, otherStation):
        // (A,B), (A,C), (A,D),
        // (B,C), (B,D),
        // (C,D)
        // because distance(A,B) = distance(B,A)

        final var tooNearStationCombination = combinations
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
            final var firstStation = tooNearStationCombination.get().getValue0();
            final var secondStation = tooNearStationCombination.get().getValue1();
            final var distance = firstStation.getLocation()
                .getKilometerDistanceTo(secondStation.getLocation());

            return Optional.of(
                new ValidationError(
                    "Distance between Station \""
                    + firstStation.getName() + "\" and \"" + secondStation.getName()
                    + "\" should be > 10 km, but was ~"
                    + (Math.round(distance * 100.0) / 100.0)
                    + " km"
                )
            );
        }

        return Optional.empty();
    }
}
