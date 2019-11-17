package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;

public class TestData {

    public static final StationName berlinHbfName = new StationName("Berlin Hbf");
    public static final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348));
    public static final TrainStation berlinHbf = new TrainStation(new StationId("1"), berlinHbfName, berlinHbfPos);

    public static final StationName berlinOstName = new StationName("Berlin Ostbahnhof");
    public static final GeoLocationInGermany berlinOstPos = new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832));
    public static final TrainStation berlinOst = new TrainStation(new StationId("2"), berlinOstName, berlinOstPos);

    public static final StationName potsdamHbfName = new StationName("Potsdam Hbf");
    public static final GeoLocationInGermany potsdamHbfPos = new GeoLocationInGermany(new GeoLocation(52.391726, 13.067120));
    public static final TrainStation potsdamHbf = new TrainStation(new StationId("3"), potsdamHbfName, potsdamHbfPos);

    public static final StationName hamburgHbfName = new StationName("Hamburg Hbf");
    public static final GeoLocationInGermany hamburgHbfPos = new GeoLocationInGermany(new GeoLocation(53.552596, 10.006727));
    public static final TrainStation hamburgHbf = new TrainStation(new StationId("4"), hamburgHbfName, hamburgHbfPos);

    public static final StationName frankfurtHbfName = new StationName("Frankfurt am Main Hbf");
    public static final GeoLocationInGermany frankfurtHbfPos = new GeoLocationInGermany(new GeoLocation(50.106880, 8.663739));
    public static final TrainStation frankfurtHbf = new TrainStation(new StationId("5"), frankfurtHbfName, frankfurtHbfPos);

    public static final StationName stuttgartHbfName = new StationName("Stuttgart Hbf");
    public static final GeoLocationInGermany stuttgartHbfPos = new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160));
    public static final TrainStation stuttgartHbf = new TrainStation(new StationId("6"), stuttgartHbfName, stuttgartHbfPos);
}
