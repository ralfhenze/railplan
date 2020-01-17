package com.ralfhenze.railplan.domain.railnetwork.presets;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.map.ImmutableMap;

import java.lang.reflect.Modifier;

public class PresetStation {

    public final static PresetStation AACHEN_HBF = new PresetStation("Aachen Hbf", 50.767802, 6.091262);
    public final static PresetStation AUGSBURG_HBF = new PresetStation("Augsburg Hbf", 48.365662, 10.886304);
    public final static PresetStation BERLIN_HBF = new PresetStation("Berlin Hbf", 52.524927, 13.369348);
    public final static PresetStation BONN_HBF = new PresetStation("Bonn Hbf", 50.732157, 7.096619);
    public final static PresetStation BREMEN_HBF = new PresetStation("Bremen Hbf", 53.082857, 8.813205);
    public final static PresetStation DORTMUND_HBF = new PresetStation("Dortmund Hbf", 51.517899, 7.458673);
    public final static PresetStation DRESDEN_HBF = new PresetStation("Dresden Hbf", 51.039879, 13.733120);
    public final static PresetStation DUESSELDORF_HBF = new PresetStation("Düsseldorf Hbf", 51.219720, 6.794286);
    public final static PresetStation ERFURT_HBF = new PresetStation("Erfurt Hbf", 50.972668, 11.037770);
    public final static PresetStation ESSEN_HBF = new PresetStation("Essen Hbf", 51.451447, 7.014698);
    public final static PresetStation FRANKFURT_HBF = new PresetStation("Frankfurt a.M. Hbf", 50.106880, 8.663739);
    public final static PresetStation FREIBURG_HBF = new PresetStation("Freiburg Hbf", 47.997183, 7.841269);
    public final static PresetStation HALLE_HBF = new PresetStation("Halle (Saale) Hbf", 51.477752, 11.987061);
    public final static PresetStation HAMBURG_HBF = new PresetStation("Hamburg Hbf", 53.552596, 10.006727);
    public final static PresetStation HANNOVER_HBF = new PresetStation("Hannover Hbf", 52.376483, 9.740987);
    public final static PresetStation HEIDELBERG_HBF = new PresetStation("Heidelberg Hbf", 49.403645, 8.675548);
    public final static PresetStation KAISERSLAUTERN_HBF = new PresetStation("Kaiserslautern Hbf", 49.436069, 7.768581);
    public final static PresetStation KARLSRUHE_HBF = new PresetStation("Karlsruhe Hbf", 48.993884, 8.401672);
    public final static PresetStation KASSEL_HBF = new PresetStation("Kassel-Wilhelmshöhe", 51.312989, 9.447111);
    public final static PresetStation KIEL_HBF = new PresetStation("Kiel Hbf", 54.315387, 10.132232);
    public final static PresetStation KOELN_HBF = new PresetStation("Köln Hbf", 50.943098, 6.958908);
    public final static PresetStation LEIPZIG_HBF = new PresetStation("Leipzig Hbf", 51.345219, 12.381685);
    public final static PresetStation MAINZ_HBF = new PresetStation("Mainz Hbf", 50.001371, 8.258817);
    public final static PresetStation MANNHEIM_HBF = new PresetStation("Mannheim Hbf", 49.479583, 8.469911);
    public final static PresetStation MUENCHEN_HBF = new PresetStation("München Hbf", 48.140038, 11.560073);
    public final static PresetStation NUERNBERG_HBF = new PresetStation("Nürnberg Hbf", 49.445496, 11.082432);
    public final static PresetStation ROSTOCK_HBF = new PresetStation("Rostock Hbf", 54.078370, 12.131861);
    public final static PresetStation SAARBRUECKEN_HBF = new PresetStation("Saarbrücken Hbf", 49.240742, 6.991088);
    public final static PresetStation STUTTGART_HBF = new PresetStation("Stuttgart Hbf", 48.784245, 9.182160);
    public final static PresetStation WOLFSBURG_HBF = new PresetStation("Wolfsburg Hbf", 52.429144, 10.787489);
    public final static PresetStation WUERZBURG_HBF = new PresetStation("Würzburg Hbf", 49.801455, 9.935910);

    private final String name;
    private final double latitude;
    private final double longitude;

    private PresetStation(final String name, final double latitude, final double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @throws IllegalArgumentException if PresetStation with stationName does not exist
     */
    public static PresetStation ofName(final String stationName) {
        final var staticFields = Lists.immutable.of(PresetStation.class.getDeclaredFields())
            .select(field -> Modifier.isStatic(field.getModifiers()))
            .collect(field -> {
                PresetStation presetStation;
                try {
                    presetStation = (PresetStation) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return presetStation;
            });

        final ImmutableMap<String, PresetStation> stations = staticFields
            .toMap(PresetStation::getName, s -> s)
            .toImmutable();

        if (!stations.containsKey(stationName)) {
            throw new IllegalArgumentException("\"" + stationName + "\" not found");
        }

        return stations.get(stationName);
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
