package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

public class IsNotNearerThan10KmTo implements ValidationConstraint<GeoLocationInGermany> {

    private final static int MINIMUM_STATION_DISTANCE_KM = 10;

    private final ImmutableList<TrainStation> otherStations;

    private static class StationDistance {
        String stationName;
        double distance;
    }

    public IsNotNearerThan10KmTo(final ImmutableList<TrainStation> otherStations) {
        this.otherStations = otherStations;
    }

    @Override
    public Optional<ValidationError> validate(
        final GeoLocationInGermany location,
        final Field field
    ) {
        final var tooNearStationDistances = otherStations
            .collect(s -> getStationDistance(s, location))
            .select(d -> d.distance <= MINIMUM_STATION_DISTANCE_KM);

        if (!tooNearStationDistances.isEmpty()) {
            return Optional.of(getValidationError(tooNearStationDistances, field));
        }

        return Optional.empty();
    }

    private StationDistance getStationDistance(
        final TrainStation station,
        final GeoLocationInGermany location
    ) {
        final var stationDistance = new StationDistance();
        stationDistance.stationName = station.getName().getName();
        stationDistance.distance = station.getLocation().getKilometerDistanceTo(location);

        return stationDistance;
    }

    private ValidationError getValidationError(
        final ImmutableList<StationDistance> tooNearStationDistances,
        final Field field
    ) {
        final var tooNearStations = tooNearStationDistances
            .collect(d -> {
                final var distance = Math.round(d.distance * 100.0) / 100.0;
                return "\"" + d.stationName + "\" (" + distance + " km)";
            })
            .castToList();

        return new ValidationError(
            "too close to " + String.join(", ", tooNearStations)
            + " (distance must be > " + MINIMUM_STATION_DISTANCE_KM + " km)",
            field
        );
    }
}
