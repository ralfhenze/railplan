package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.map.ImmutableMap;

import java.time.LocalDate;

public class TestData {

    public static final String berlinHbfName = "Berlin Hbf";
    public static final double berlinHbfLat = 52.524927;
    public static final double berlinHbfLng = 13.369348;
    public static final TrainStation berlinHbf = new TrainStation(
        new TrainStationId(1),
        new TrainStationName(berlinHbfName),
        new GeoLocationInGermany(berlinHbfLat, berlinHbfLng)
    );

    public static final String berlinOstName = "Berlin Ostbahnhof";
    public static final double berlinOstLat = 52.510784;
    public static final double berlinOstLng = 13.434832;
    public static final TrainStation berlinOst = new TrainStation(
        new TrainStationId(2),
        new TrainStationName(berlinOstName),
        new GeoLocationInGermany(berlinOstLat, berlinOstLng)
    );

    public static final String potsdamHbfName = "Potsdam Hbf";
    public static final double potsdamHbfLat = 52.391726;
    public static final double potsdamHbfLng = 13.067120;
    public static final TrainStation potsdamHbf = new TrainStation(
        new TrainStationId(3),
        new TrainStationName(potsdamHbfName),
        new GeoLocationInGermany(potsdamHbfLat, potsdamHbfLng)
    );

    public static final String hamburgHbfName = "Hamburg Hbf";
    public static final double hamburgHbfLat = 53.552596;
    public static final double hamburgHbfLng = 10.006727;
    public static final TrainStation hamburgHbf = new TrainStation(
        new TrainStationId(4),
        new TrainStationName(hamburgHbfName),
        new GeoLocationInGermany(hamburgHbfLat, hamburgHbfLng)
    );

    public static final String frankfurtHbfName = "Frankfurt am Main Hbf";
    public static final double frankfurtHbfLat = 50.106880;
    public static final double frankfurtHbfLng = 8.663739;
    public static final TrainStation frankfurtHbf = new TrainStation(
        new TrainStationId(5),
        new TrainStationName(frankfurtHbfName),
        new GeoLocationInGermany(frankfurtHbfLat, frankfurtHbfLng)
    );

    public static final String stuttgartHbfName = "Stuttgart Hbf";
    public static final double stuttgartHbfLat = 48.784245;
    public static final double stuttgartHbfLng = 9.182160;
    public static final TrainStation stuttgartHbf = new TrainStation(
        new TrainStationId(6),
        new TrainStationName(stuttgartHbfName),
        new GeoLocationInGermany(stuttgartHbfLat, stuttgartHbfLng)
    );

    public static final ValidityPeriod defaultPeriod = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static TrainStation getStation(String stationName) {
        ImmutableMap<String, TrainStation> stations =
            Sets.immutable.of(
                berlinHbf,
                berlinOst,
                potsdamHbf,
                hamburgHbf,
                frankfurtHbf,
                stuttgartHbf
            )
            .toMap(s -> s.getName().getName(), s -> s)
            .toImmutable();

        if (!stations.containsKey(stationName)) {
            throw new IllegalArgumentException("\"" + stationName + "\" not found in TestData");
        }

        return stations.get(stationName);
    }
}
