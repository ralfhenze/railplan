package com.ralfhenze.railplan.userinterface.web;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultTracks {

    public class Track {
        public final DefaultStation station1;
        public final DefaultStation station2;

        Track(DefaultStation station1, DefaultStation station2) {
            this.station1 = station1;
            this.station2 = station2;
        }
    }

    private final List<Track> tracks = List.of(
        new Track(DefaultStation.HAMBURG_HBF, DefaultStation.KIEL_HBF),
        new Track(DefaultStation.HAMBURG_HBF, DefaultStation.ROSTOCK_HBF),
        new Track(DefaultStation.HAMBURG_HBF, DefaultStation.BREMEN_HBF),
        new Track(DefaultStation.HAMBURG_HBF, DefaultStation.HANNOVER_HBF),
        new Track(DefaultStation.HAMBURG_HBF, DefaultStation.BERLIN_HBF),
        new Track(DefaultStation.HANNOVER_HBF, DefaultStation.BREMEN_HBF),
        new Track(DefaultStation.HANNOVER_HBF, DefaultStation.WOLFSBURG_HBF),
        new Track(DefaultStation.HANNOVER_HBF, DefaultStation.KASSEL_HBF),
        new Track(DefaultStation.HANNOVER_HBF, DefaultStation.DORTMUND_HBF),
        new Track(DefaultStation.BERLIN_HBF, DefaultStation.ROSTOCK_HBF),
        new Track(DefaultStation.BERLIN_HBF, DefaultStation.WOLFSBURG_HBF),
        new Track(DefaultStation.BERLIN_HBF, DefaultStation.HALLE_HBF),
        new Track(DefaultStation.BERLIN_HBF, DefaultStation.LEIPZIG_HBF),
        new Track(DefaultStation.BERLIN_HBF, DefaultStation.DRESDEN_HBF),
        new Track(DefaultStation.LEIPZIG_HBF, DefaultStation.DRESDEN_HBF),
        new Track(DefaultStation.LEIPZIG_HBF, DefaultStation.ERFURT_HBF),
        new Track(DefaultStation.ERFURT_HBF, DefaultStation.HALLE_HBF),
        new Track(DefaultStation.ERFURT_HBF, DefaultStation.FRANKFURT_HBF),
        new Track(DefaultStation.ERFURT_HBF, DefaultStation.NUERNBERG_HBF),
        new Track(DefaultStation.KASSEL_HBF, DefaultStation.DORTMUND_HBF),
        new Track(DefaultStation.KASSEL_HBF, DefaultStation.ERFURT_HBF),
        new Track(DefaultStation.KASSEL_HBF, DefaultStation.FRANKFURT_HBF),
        new Track(DefaultStation.FRANKFURT_HBF, DefaultStation.WUERZBURG_HBF),
        new Track(DefaultStation.FRANKFURT_HBF, DefaultStation.MAINZ_HBF),
        new Track(DefaultStation.FRANKFURT_HBF, DefaultStation.MANNHEIM_HBF),
        new Track(DefaultStation.FRANKFURT_HBF, DefaultStation.HEIDELBERG_HBF),
        new Track(DefaultStation.FRANKFURT_HBF, DefaultStation.BONN_HBF),
        new Track(DefaultStation.MAINZ_HBF, DefaultStation.BONN_HBF),
        new Track(DefaultStation.MAINZ_HBF, DefaultStation.MANNHEIM_HBF),
        new Track(DefaultStation.KOELN_HBF, DefaultStation.BONN_HBF),
        new Track(DefaultStation.KOELN_HBF, DefaultStation.AACHEN_HBF),
        new Track(DefaultStation.KOELN_HBF, DefaultStation.DUESSELDORF_HBF),
        new Track(DefaultStation.KOELN_HBF, DefaultStation.DORTMUND_HBF),
        new Track(DefaultStation.ESSEN_HBF, DefaultStation.DUESSELDORF_HBF),
        new Track(DefaultStation.ESSEN_HBF, DefaultStation.DORTMUND_HBF),
        new Track(DefaultStation.MANNHEIM_HBF, DefaultStation.KAISERSLAUTERN_HBF),
        new Track(DefaultStation.KAISERSLAUTERN_HBF, DefaultStation.SAARBRUECKEN_HBF),
        new Track(DefaultStation.MANNHEIM_HBF, DefaultStation.KARLSRUHE_HBF),
        new Track(DefaultStation.KARLSRUHE_HBF, DefaultStation.FREIBURG_HBF),
        new Track(DefaultStation.KARLSRUHE_HBF, DefaultStation.STUTTGART_HBF),
        new Track(DefaultStation.STUTTGART_HBF, DefaultStation.MANNHEIM_HBF),
        new Track(DefaultStation.STUTTGART_HBF, DefaultStation.HEIDELBERG_HBF),
        new Track(DefaultStation.STUTTGART_HBF, DefaultStation.AUGSBURG_HBF),
        new Track(DefaultStation.NUERNBERG_HBF, DefaultStation.WUERZBURG_HBF),
        new Track(DefaultStation.NUERNBERG_HBF, DefaultStation.AUGSBURG_HBF),
        new Track(DefaultStation.MUENCHEN_HBF, DefaultStation.NUERNBERG_HBF),
        new Track(DefaultStation.MUENCHEN_HBF, DefaultStation.AUGSBURG_HBF)
    );

    public List<Track> getTracks() {
        return tracks.stream()
            .sorted(Comparator.comparing(track -> track.station1.name + track.station2.name))
            .collect(Collectors.toList());
    }
}
