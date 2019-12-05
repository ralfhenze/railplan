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

    public static final TrainStationName berlinHbfName = new TrainStationName("Berlin Hbf");
    public static final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(52.524927, 13.369348);
    public static final TrainStation berlinHbf = new TrainStation(new TrainStationId("1"), berlinHbfName, berlinHbfPos);

    public static final TrainStationName berlinOstName = new TrainStationName("Berlin Ostbahnhof");
    public static final GeoLocationInGermany berlinOstPos = new GeoLocationInGermany(52.510784, 13.434832);
    public static final TrainStation berlinOst = new TrainStation(new TrainStationId("2"), berlinOstName, berlinOstPos);

    public static final TrainStationName potsdamHbfName = new TrainStationName("Potsdam Hbf");
    public static final GeoLocationInGermany potsdamHbfPos = new GeoLocationInGermany(52.391726, 13.067120);
    public static final TrainStation potsdamHbf = new TrainStation(new TrainStationId("3"), potsdamHbfName, potsdamHbfPos);

    public static final TrainStationName hamburgHbfName = new TrainStationName("Hamburg Hbf");
    public static final GeoLocationInGermany hamburgHbfPos = new GeoLocationInGermany(53.552596, 10.006727);
    public static final TrainStation hamburgHbf = new TrainStation(new TrainStationId("4"), hamburgHbfName, hamburgHbfPos);

    public static final TrainStationName frankfurtHbfName = new TrainStationName("Frankfurt am Main Hbf");
    public static final GeoLocationInGermany frankfurtHbfPos = new GeoLocationInGermany(50.106880, 8.663739);
    public static final TrainStation frankfurtHbf = new TrainStation(new TrainStationId("5"), frankfurtHbfName, frankfurtHbfPos);

    public static final TrainStationName stuttgartHbfName = new TrainStationName("Stuttgart Hbf");
    public static final GeoLocationInGermany stuttgartHbfPos = new GeoLocationInGermany(48.784245, 9.182160);
    public static final TrainStation stuttgartHbf = new TrainStation(new TrainStationId("6"), stuttgartHbfName, stuttgartHbfPos);

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

    public static final ValidityPeriod defaultPeriod = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );
}
