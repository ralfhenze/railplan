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

    public static final TrainStationName berlinHbfName = new TrainStationName("Berlin Hbf");
    public static final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(52.524927, 13.369348);
    public static final TrainStation berlinHbf = new TrainStation(new TrainStationId(1), berlinHbfName, berlinHbfPos);

    public static final TrainStationName potsdamHbfName = new TrainStationName("Potsdam Hbf");
    public static final GeoLocationInGermany potsdamHbfPos = new GeoLocationInGermany(52.391726, 13.067120);

    public static final TrainStationName hamburgHbfName = new TrainStationName("Hamburg Hbf");
    public static final GeoLocationInGermany hamburgHbfPos = new GeoLocationInGermany(53.552596, 10.006727);
    public static final TrainStation hamburgHbf = new TrainStation(new TrainStationId(2), hamburgHbfName, hamburgHbfPos);

    public static final ValidityPeriod defaultPeriod = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static final RailNetworkDraft berlinHamburgDraft = new RailNetworkDraft()
        .withId(new RailNetworkDraftId("123"))
        .withNewStation(berlinHbfName, berlinHbfPos)
        .withNewStation(hamburgHbfName, hamburgHbfPos)
        .withNewTrack(berlinHbfName, hamburgHbfName);
}
