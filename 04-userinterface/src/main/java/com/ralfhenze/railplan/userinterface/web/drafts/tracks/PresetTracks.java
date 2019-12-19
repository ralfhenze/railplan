package com.ralfhenze.railplan.userinterface.web.drafts.tracks;

import com.ralfhenze.railplan.userinterface.web.drafts.stations.PresetStation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PresetTracks {

    public class Track {
        public final PresetStation station1;
        public final PresetStation station2;

        Track(PresetStation station1, PresetStation station2) {
            this.station1 = station1;
            this.station2 = station2;
        }
    }

    private final List<Track> tracks = List.of(
        new Track(PresetStation.HAMBURG_HBF, PresetStation.KIEL_HBF),
        new Track(PresetStation.HAMBURG_HBF, PresetStation.ROSTOCK_HBF),
        new Track(PresetStation.HAMBURG_HBF, PresetStation.BREMEN_HBF),
        new Track(PresetStation.HAMBURG_HBF, PresetStation.HANNOVER_HBF),
        new Track(PresetStation.HAMBURG_HBF, PresetStation.BERLIN_HBF),
        new Track(PresetStation.HANNOVER_HBF, PresetStation.BREMEN_HBF),
        new Track(PresetStation.HANNOVER_HBF, PresetStation.WOLFSBURG_HBF),
        new Track(PresetStation.HANNOVER_HBF, PresetStation.KASSEL_HBF),
        new Track(PresetStation.HANNOVER_HBF, PresetStation.DORTMUND_HBF),
        new Track(PresetStation.BERLIN_HBF, PresetStation.ROSTOCK_HBF),
        new Track(PresetStation.BERLIN_HBF, PresetStation.WOLFSBURG_HBF),
        new Track(PresetStation.BERLIN_HBF, PresetStation.HALLE_HBF),
        new Track(PresetStation.BERLIN_HBF, PresetStation.LEIPZIG_HBF),
        new Track(PresetStation.BERLIN_HBF, PresetStation.DRESDEN_HBF),
        new Track(PresetStation.LEIPZIG_HBF, PresetStation.DRESDEN_HBF),
        new Track(PresetStation.LEIPZIG_HBF, PresetStation.ERFURT_HBF),
        new Track(PresetStation.ERFURT_HBF, PresetStation.HALLE_HBF),
        new Track(PresetStation.ERFURT_HBF, PresetStation.FRANKFURT_HBF),
        new Track(PresetStation.ERFURT_HBF, PresetStation.NUERNBERG_HBF),
        new Track(PresetStation.KASSEL_HBF, PresetStation.DORTMUND_HBF),
        new Track(PresetStation.KASSEL_HBF, PresetStation.ERFURT_HBF),
        new Track(PresetStation.KASSEL_HBF, PresetStation.FRANKFURT_HBF),
        new Track(PresetStation.FRANKFURT_HBF, PresetStation.WUERZBURG_HBF),
        new Track(PresetStation.FRANKFURT_HBF, PresetStation.MAINZ_HBF),
        new Track(PresetStation.FRANKFURT_HBF, PresetStation.MANNHEIM_HBF),
        new Track(PresetStation.FRANKFURT_HBF, PresetStation.HEIDELBERG_HBF),
        new Track(PresetStation.FRANKFURT_HBF, PresetStation.BONN_HBF),
        new Track(PresetStation.MAINZ_HBF, PresetStation.BONN_HBF),
        new Track(PresetStation.MAINZ_HBF, PresetStation.MANNHEIM_HBF),
        new Track(PresetStation.KOELN_HBF, PresetStation.BONN_HBF),
        new Track(PresetStation.KOELN_HBF, PresetStation.AACHEN_HBF),
        new Track(PresetStation.KOELN_HBF, PresetStation.DUESSELDORF_HBF),
        new Track(PresetStation.KOELN_HBF, PresetStation.DORTMUND_HBF),
        new Track(PresetStation.ESSEN_HBF, PresetStation.DUESSELDORF_HBF),
        new Track(PresetStation.ESSEN_HBF, PresetStation.DORTMUND_HBF),
        new Track(PresetStation.MANNHEIM_HBF, PresetStation.KAISERSLAUTERN_HBF),
        new Track(PresetStation.KAISERSLAUTERN_HBF, PresetStation.SAARBRUECKEN_HBF),
        new Track(PresetStation.MANNHEIM_HBF, PresetStation.KARLSRUHE_HBF),
        new Track(PresetStation.KARLSRUHE_HBF, PresetStation.FREIBURG_HBF),
        new Track(PresetStation.KARLSRUHE_HBF, PresetStation.STUTTGART_HBF),
        new Track(PresetStation.STUTTGART_HBF, PresetStation.MANNHEIM_HBF),
        new Track(PresetStation.STUTTGART_HBF, PresetStation.HEIDELBERG_HBF),
        new Track(PresetStation.STUTTGART_HBF, PresetStation.AUGSBURG_HBF),
        new Track(PresetStation.NUERNBERG_HBF, PresetStation.WUERZBURG_HBF),
        new Track(PresetStation.NUERNBERG_HBF, PresetStation.AUGSBURG_HBF),
        new Track(PresetStation.MUENCHEN_HBF, PresetStation.NUERNBERG_HBF),
        new Track(PresetStation.MUENCHEN_HBF, PresetStation.AUGSBURG_HBF)
    );

    public List<Track> getAllPresetTracks() {
        return tracks.stream()
            .sorted(Comparator.comparing(track -> track.station1.name + track.station2.name))
            .collect(Collectors.toList());
    }

    public Optional<Track> getTrackOfId(int trackId) {
        return Optional.ofNullable(getAllPresetTracks().get(trackId));
    }
}
