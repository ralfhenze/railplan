package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultStations {

    public List<String> getStationNames() {
        return Stream.of(DefaultStation.values())
            .map(station -> station.name)
            .sorted()
            .collect(Collectors.toList());
    }

    public List<Double> getCoordinatesOf(final String stationName) {
        final Map<String, List<Double>> lut = Stream.of(DefaultStation.values())
            .collect(Collectors.toMap(s -> s.name, s -> List.of(s.latitude, s.longitude)));

        return lut.getOrDefault(stationName, List.of());
    }
}
