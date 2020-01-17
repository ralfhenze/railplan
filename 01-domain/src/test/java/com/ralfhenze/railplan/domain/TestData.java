package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.map.ImmutableMap;

import java.time.LocalDate;

public class TestData {

    public static final String BERLIN_HBF_NAME = "Berlin Hbf";
    public static final double BERLIN_HBF_LAT = 52.524927;
    public static final double BERLIN_HBF_LNG = 13.369348;
    public static final TrainStation BERLIN_HBF = new TrainStation(
        new TrainStationId(1),
        new TrainStationName(BERLIN_HBF_NAME),
        new GeoLocationInGermany(BERLIN_HBF_LAT, BERLIN_HBF_LNG)
    );

    public static final String BERLIN_OST_NAME = "Berlin Ostbahnhof";
    public static final double BERLIN_OST_LAT = 52.510784;
    public static final double BERLIN_OST_LNG = 13.434832;
    public static final TrainStation BERLIN_OST = new TrainStation(
        new TrainStationId(2),
        new TrainStationName(BERLIN_OST_NAME),
        new GeoLocationInGermany(BERLIN_OST_LAT, BERLIN_OST_LNG)
    );

    public static final String POTSDAM_HBF_NAME = "Potsdam Hbf";
    public static final double POTSDAM_HBF_LAT = 52.391726;
    public static final double POTSDAM_HBF_LNG = 13.067120;
    public static final TrainStation POTSDAM_HBF = new TrainStation(
        new TrainStationId(3),
        new TrainStationName(POTSDAM_HBF_NAME),
        new GeoLocationInGermany(POTSDAM_HBF_LAT, POTSDAM_HBF_LNG)
    );

    public static final String HAMBURG_HBF_NAME = "Hamburg Hbf";
    public static final double HAMBURG_HBF_LAT = 53.552596;
    public static final double HAMBURG_HBF_LNG = 10.006727;
    public static final TrainStation HAMBURG_HBF = new TrainStation(
        new TrainStationId(4),
        new TrainStationName(HAMBURG_HBF_NAME),
        new GeoLocationInGermany(HAMBURG_HBF_LAT, HAMBURG_HBF_LNG)
    );

    public static final String FRANKFURT_HBF_NAME = "Frankfurt am Main Hbf";
    public static final double FRANKFURT_HBF_LAT = 50.106880;
    public static final double FRANKFURT_HBF_LNG = 8.663739;
    public static final TrainStation FRANKFURT_HBF = new TrainStation(
        new TrainStationId(5),
        new TrainStationName(FRANKFURT_HBF_NAME),
        new GeoLocationInGermany(FRANKFURT_HBF_LAT, FRANKFURT_HBF_LNG)
    );

    public static final String STUTTGART_HBF_NAME = "Stuttgart Hbf";
    public static final double STUTTGART_HBF_LAT = 48.784245;
    public static final double STUTTGART_HBF_LNG = 9.182160;
    public static final TrainStation STUTTGART_HBF = new TrainStation(
        new TrainStationId(6),
        new TrainStationName(STUTTGART_HBF_NAME),
        new GeoLocationInGermany(STUTTGART_HBF_LAT, STUTTGART_HBF_LNG)
    );

    public static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static TrainStation getStation(String stationName) {
        ImmutableMap<String, TrainStation> stations =
            Sets.immutable.of(
                BERLIN_HBF,
                BERLIN_OST,
                POTSDAM_HBF,
                HAMBURG_HBF,
                FRANKFURT_HBF,
                STUTTGART_HBF
            )
            .toMap(s -> s.getName().getName(), s -> s)
            .toImmutable();

        if (!stations.containsKey(stationName)) {
            throw new IllegalArgumentException("\"" + stationName + "\" not found in TestData");
        }

        return stations.get(stationName);
    }

    public static RailNetworkDraft getDraftWith(TrainStation... stations) {
        var draft = new RailNetworkDraft();
        for (final var station : stations) {
            draft = draft.withNewStation(
                station.getName().getName(),
                station.getLocation().getLatitude(),
                station.getLocation().getLongitude()
            );
        }

        return draft;
    }
}
