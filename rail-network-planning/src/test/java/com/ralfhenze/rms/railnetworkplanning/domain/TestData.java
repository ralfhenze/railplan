package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.*;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.RailNetworkPeriod;

import java.time.LocalDate;

public class TestData {

    public static final TrainStationName berlinHbfName = new TrainStationName("Berlin Hbf");
    public static final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348));
    public static final TrainStation berlinHbf = new TrainStation(new TrainStationId("1"), berlinHbfName, berlinHbfPos);

    public static final TrainStationName berlinOstName = new TrainStationName("Berlin Ostbahnhof");
    public static final GeoLocationInGermany berlinOstPos = new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832));
    public static final TrainStation berlinOst = new TrainStation(new TrainStationId("2"), berlinOstName, berlinOstPos);

    public static final TrainStationName potsdamHbfName = new TrainStationName("Potsdam Hbf");
    public static final GeoLocationInGermany potsdamHbfPos = new GeoLocationInGermany(new GeoLocation(52.391726, 13.067120));
    public static final TrainStation potsdamHbf = new TrainStation(new TrainStationId("3"), potsdamHbfName, potsdamHbfPos);

    public static final TrainStationName hamburgHbfName = new TrainStationName("Hamburg Hbf");
    public static final GeoLocationInGermany hamburgHbfPos = new GeoLocationInGermany(new GeoLocation(53.552596, 10.006727));
    public static final TrainStation hamburgHbf = new TrainStation(new TrainStationId("4"), hamburgHbfName, hamburgHbfPos);

    public static final TrainStationName frankfurtHbfName = new TrainStationName("Frankfurt am Main Hbf");
    public static final GeoLocationInGermany frankfurtHbfPos = new GeoLocationInGermany(new GeoLocation(50.106880, 8.663739));
    public static final TrainStation frankfurtHbf = new TrainStation(new TrainStationId("5"), frankfurtHbfName, frankfurtHbfPos);

    public static final TrainStationName stuttgartHbfName = new TrainStationName("Stuttgart Hbf");
    public static final GeoLocationInGermany stuttgartHbfPos = new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160));
    public static final TrainStation stuttgartHbf = new TrainStation(new TrainStationId("6"), stuttgartHbfName, stuttgartHbfPos);

    public static final RailNetworkPeriod defaultPeriod = new RailNetworkPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );
}
