package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Collection;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

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
        final TrainStationName duplicateStationName = stations
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
