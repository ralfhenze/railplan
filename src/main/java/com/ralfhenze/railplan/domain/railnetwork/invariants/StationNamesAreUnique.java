package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Collection;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

public class StationNamesAreUnique implements Invariant {

    @Override
    public void ensureIsSatisfied(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        ensureNotNull(stations, "Train Stations");

        if (stations.size() >= 2) {
            ensureUniqueStationNames(stations);
        }
    }

    private void ensureUniqueStationNames(final ImmutableList<TrainStation> stations) {
        final var duplicateStationName = stations
            .groupBy(TrainStation::getName)
            .selectKeysMultiValues((name, s) -> ((Collection)s).size() > 1)
            .keyBag()
            .getAny();

        if (duplicateStationName != null) {
            throw new IllegalArgumentException(
                "Station Name \"" + duplicateStationName + "\" already exists"
            );
        }
    }
}
