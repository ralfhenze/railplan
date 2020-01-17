package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

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

    public static final String HAMBURG_HBF_NAME = "Hamburg Hbf";
    public static final double HAMBURG_HBF_LAT = 53.552596;
    public static final double HAMBURG_HBF_LNG = 10.006727;
    public static final TrainStation HAMBURG_HBF = new TrainStation(
        new TrainStationId(2),
        new TrainStationName(HAMBURG_HBF_NAME),
        new GeoLocationInGermany(HAMBURG_HBF_LAT, HAMBURG_HBF_LNG)
    );

    public static final String POTSDAM_HBF_NAME = "Potsdam Hbf";
    public static final double POTSDAM_HBF_LAT = 52.391726;
    public static final double POTSDAM_HBF_LNG = 13.067120;

    public static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static final RailNetworkDraft BERLIN_HAMBURG_DRAFT = new RailNetworkDraft()
        .withId(new RailNetworkDraftId("123"))
        .withNewStation(BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG)
        .withNewStation(HAMBURG_HBF_NAME, HAMBURG_HBF_LAT, HAMBURG_HBF_LNG)
        .withNewTrack(BERLIN_HBF_NAME, HAMBURG_HBF_NAME);
}
