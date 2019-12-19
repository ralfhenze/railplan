package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import java.util.Optional;

public enum PresetStation {
    AACHEN_HBF("Aachen Hbf", 50.767802, 6.091262),
    AUGSBURG_HBF("Augsburg Hbf", 48.365662, 10.886304),
    BERLIN_HBF("Berlin Hbf", 52.524927, 13.369348),
    BONN_HBF("Bonn Hbf", 50.732157, 7.096619),
    BREMEN_HBF("Bremen Hbf", 53.082857, 8.813205),
    DORTMUND_HBF("Dortmund Hbf", 51.517899, 7.458673),
    DRESDEN_HBF("Dresden Hbf", 51.039879, 13.733120),
    DUESSELDORF_HBF("Düsseldorf Hbf", 51.219720, 6.794286),
    ERFURT_HBF("Erfurt Hbf", 50.972668, 11.037770),
    ESSEN_HBF("Essen Hbf", 51.451447, 7.014698),
    FRANKFURT_HBF("Frankfurt a.M. Hbf", 50.106880, 8.663739),
    FREIBURG_HBF("Freiburg Hbf", 47.997183, 7.841269),
    HALLE_HBF("Halle (Saale) Hbf", 51.477752, 11.987061),
    HAMBURG_HBF("Hamburg Hbf", 53.552596, 10.006727),
    HANNOVER_HBF("Hannover Hbf", 52.376483, 9.740987),
    HEIDELBERG_HBF("Heidelberg Hbf", 49.403645, 8.675548),
    KAISERSLAUTERN_HBF("Kaiserslautern Hbf", 49.436069, 7.768581),
    KARLSRUHE_HBF("Karlsruhe Hbf", 48.993884, 8.401672),
    KASSEL_HBF("Kassel-Wilhelmshöhe", 51.312989, 9.447111),
    KIEL_HBF("Kiel Hbf", 54.315387, 10.132232),
    KOELN_HBF("Köln Hbf", 50.943098, 6.958908),
    LEIPZIG_HBF("Leipzig Hbf", 51.345219, 12.381685),
    MAINZ_HBF("Mainz Hbf", 50.001371, 8.258817),
    MANNHEIM_HBF("Mannheim Hbf", 49.479583, 8.469911),
    MUENCHEN_HBF("München Hbf", 48.140038, 11.560073),
    NUERNBERG_HBF("Nürnberg Hbf", 49.445496, 11.082432),
    ROSTOCK_HBF("Rostock Hbf", 54.078370, 12.131861),
    SAARBRUECKEN_HBF("Saarbrücken Hbf", 49.240742, 6.991088),
    STUTTGART_HBF("Stuttgart Hbf", 48.784245, 9.182160),
    WOLFSBURG_HBF("Wolfsburg Hbf", 52.429144, 10.787489),
    WUERZBURG_HBF("Würzburg Hbf", 49.801455, 9.935910);

    public final String name;
    public final double latitude;
    public final double longitude;

    PresetStation(final String name, final double latitude, final double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Optional<PresetStation> getOptionalOf(final String name) {
        try {
            final var presetStation = PresetStation.valueOf(name);
            return Optional.of(presetStation);
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
