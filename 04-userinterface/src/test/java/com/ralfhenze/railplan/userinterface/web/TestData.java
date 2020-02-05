package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;

import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.HAMBURG_HBF;

public class TestData {

    public static final TrainStation BERLIN_HBF_STATION = new TrainStation(
        new TrainStationId(1),
        new TrainStationName(BERLIN_HBF.getName()),
        new GeoLocationInGermany(BERLIN_HBF.getLatitude(), BERLIN_HBF.getLongitude())
    );

    public static final TrainStation HAMBURG_HBF_STATION = new TrainStation(
        new TrainStationId(2),
        new TrainStationName(HAMBURG_HBF.getName()),
        new GeoLocationInGermany(HAMBURG_HBF.getLatitude(), HAMBURG_HBF.getLongitude())
    );

    public static final RailNetwork BERLIN_HAMBURG_NETWORK = new RailNetwork()
        .addStations(BERLIN_HBF, HAMBURG_HBF)
        .addTrackBetween(BERLIN_HBF, HAMBURG_HBF)
        .withId(new RailNetworkId("123"));
}
