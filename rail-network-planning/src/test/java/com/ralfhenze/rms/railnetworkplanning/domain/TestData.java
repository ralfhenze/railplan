package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;

class TestData {

    static final StationName berlinHbfName = new StationName("Berlin Hbf");
    static final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348));
    static final TrainStation berlinHbf = new TrainStation(new StationId("1"), berlinHbfName, berlinHbfPos);

    static final StationName berlinOstName = new StationName("Berlin Ostbahnhof");
    static final GeoLocationInGermany berlinOstPos = new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832));
    static final TrainStation berlinOst = new TrainStation(new StationId("2"), berlinOstName, berlinOstPos);

    static final StationName potsdamHbfName = new StationName("Potsdam Hbf");
    static final GeoLocationInGermany potsdamHbfPos = new GeoLocationInGermany(new GeoLocation(52.391726, 13.067120));
    static final TrainStation potsdamHbf = new TrainStation(new StationId("3"), potsdamHbfName, potsdamHbfPos);

    static final StationName hamburgHbfName = new StationName("Hamburg Hbf");
    static final GeoLocationInGermany hamburgHbfPos = new GeoLocationInGermany(new GeoLocation(53.552596, 10.006727));
    static final TrainStation hamburgHbf = new TrainStation(new StationId("4"), hamburgHbfName, hamburgHbfPos);

    static final StationName frankfurtHbfName = new StationName("Frankfurt am Main Hbf");
    static final GeoLocationInGermany frankfurtHbfPos = new GeoLocationInGermany(new GeoLocation(50.106880, 8.663739));
    static final TrainStation frankfurtHbf = new TrainStation(new StationId("5"), frankfurtHbfName, frankfurtHbfPos);

    static final StationName stuttgartHbfName = new StationName("Stuttgart Hbf");
    static final GeoLocationInGermany stuttgartHbfPos = new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160));
    static final TrainStation stuttgartHbf = new TrainStation(new StationId("6"), stuttgartHbfName, stuttgartHbfPos);
}
