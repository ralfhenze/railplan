package com.ralfhenze.railplan.userinterface.web;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultStations {

    private class Station {
        public final String name;
        public final double latitude;
        public final double longitude;

        public Station(final String name, final double latitude, final double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private final List<Station> stations = List.of(
        new Station("Kiel Hbf", 54.315387, 10.132232),
        new Station("Rostock Hbf", 54.078370, 12.131861),
        new Station("Hamburg Hbf", 53.552596, 10.006727),
        new Station("Bremen Hbf", 53.082857, 8.813205),
        new Station("Berlin Hbf", 52.524927, 13.369348),
        new Station("Hannover Hbf", 52.376483, 9.740987),
        new Station("Wolfsburg Hbf", 52.429144, 10.787489),
        new Station("Halle (Saale) Hbf", 51.477752, 11.987061),
        new Station("Leipzig Hbf", 51.345219, 12.381685),
        new Station("Dresden Hbf", 51.039879, 13.733120),
        new Station("Erfurt Hbf", 50.972668, 11.037770),
        new Station("Kassel-Wilhelmshöhe", 51.312989, 9.447111),
        new Station("Würzburg Hbf", 49.801455, 9.935910),
        new Station("Nürnberg Hbf", 49.445496, 11.082432),
        new Station("München Hbf", 48.140038, 11.560073),
        new Station("Augsburg Hbf", 48.365662, 10.886304),
        new Station("Stuttgart Hbf", 48.784245, 9.182160),
        new Station("Freiburg Hbf", 47.997183, 7.841269),
        new Station("Karlsruhe Hbf", 48.993884, 8.401672),
        new Station("Mannheim Hbf", 49.479583, 8.469911),
        new Station("Heidelberg Hbf", 49.403645, 8.675548),
        new Station("Kaiserslautern Hbf", 49.436069, 7.768581),
        new Station("Saarbrücken Hbf", 49.240742, 6.991088),
        new Station("Frankfurt a.M. Hbf", 50.106880, 8.663739),
        new Station("Mainz Hbf", 50.001371, 8.258817),
        new Station("Bonn Hbf", 50.732157, 7.096619),
        new Station("Köln Hbf", 50.943098, 6.958908),
        new Station("Aachen Hbf", 50.767802, 6.091262),
        new Station("Düsseldorf Hbf", 51.219720, 6.794286),
        new Station("Essen Hbf", 51.451447, 7.014698),
        new Station("Dortmund Hbf", 51.517899, 7.458673)
    );

    public List<String> getStationNames() {
        return stations.stream()
            .map(station -> station.name)
            .sorted()
            .collect(Collectors.toList());
    }
}
