package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;

class TestData {

    static final TrainStation berlinHbf = new TrainStation(
        new StationId("1"),
        new StationName("Berlin Hbf"),
        new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348))
    );
    static final TrainStation hamburgHbf = new TrainStation(
        new StationId("2"),
        new StationName("Hamburg Hbf"),
        new GeoLocationInGermany(new GeoLocation(53.552596, 10.006727))
    );
    static final TrainStation frankfurtHbf = new TrainStation(
        new StationId("3"),
        new StationName("Frankfurt am Main Hbf"),
        new GeoLocationInGermany(new GeoLocation(50.106880, 8.663739))
    );
    static final TrainStation stuttgartHbf = new TrainStation(
        new StationId("4"),
        new StationName("Stuttgart Hbf"),
        new GeoLocationInGermany(new GeoLocation(48.784245, 9.182160))
    );
    static final TrainStation berlinOstbahnhof = new TrainStation(
        new StationId("5"),
        new StationName("Berlin Ostbahnhof"),
        new GeoLocationInGermany(new GeoLocation(52.510784, 13.434832))
    );
}
