package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class StationNamesAreUnique implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<RailwayTrack> tracks) {
        ensureNotNull(stations, "Train Stations");

        if (stations.size() >= 2) {
            ensureUniqueStationNames(stations);
        }
    }

    private void ensureUniqueStationNames(Set<TrainStation> stations) {
        // Map<Station name, How many times it occurs>
        final Map<String, Long> countPerName = stations.stream()
            .collect(
                Collectors.groupingBy(
                    station -> station.getName().getName(),
                    Collectors.counting()
                )
            );

        final Optional<String> duplicateStationName = countPerName.entrySet().stream()
            .filter(e -> e.getValue() > 1)
            .map(Map.Entry::getKey)
            .findAny();

        if (duplicateStationName.isPresent()) {
            throw new IllegalArgumentException(
                "Station Name \"" + duplicateStationName.get() + "\" already exists"
            );
        }
    }
}
