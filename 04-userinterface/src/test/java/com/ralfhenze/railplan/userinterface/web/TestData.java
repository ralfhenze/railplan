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

    public static final String berlinHbfName = "Berlin Hbf";
    public static final double berlinHbfLat = 52.524927;
    public static final double berlinHbfLng = 13.369348;
    public static final TrainStation berlinHbf = new TrainStation(
        new TrainStationId(1),
        new TrainStationName(berlinHbfName),
        new GeoLocationInGermany(berlinHbfLat, berlinHbfLng)
    );

    public static final String hamburgHbfName = "Hamburg Hbf";
    public static final double hamburgHbfLat = 53.552596;
    public static final double hamburgHbfLng = 10.006727;
    public static final TrainStation hamburgHbf = new TrainStation(
        new TrainStationId(2),
        new TrainStationName(hamburgHbfName),
        new GeoLocationInGermany(hamburgHbfLat, hamburgHbfLng)
    );

    public static final String potsdamHbfName = "Potsdam Hbf";
    public static final double potsdamHbfLat = 52.391726;
    public static final double potsdamHbfLng = 13.067120;

    public static final ValidityPeriod defaultPeriod = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static final RailNetworkDraft berlinHamburgDraft = new RailNetworkDraft()
        .withId(new RailNetworkDraftId("123"))
        .withNewStation(berlinHbfName, berlinHbfLat, berlinHbfLng)
        .withNewStation(hamburgHbfName, hamburgHbfLat, hamburgHbfLng)
        .withNewTrack(berlinHbfName, hamburgHbfName);
}
