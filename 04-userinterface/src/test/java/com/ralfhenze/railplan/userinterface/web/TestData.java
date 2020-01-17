package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

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

    public static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static final RailNetworkDraft BERLIN_HAMBURG_DRAFT = RailNetworkDraft
        .of(BERLIN_HBF, HAMBURG_HBF)
        .withNewTrack(BERLIN_HBF.getName(), HAMBURG_HBF.getName())
        .withId(new RailNetworkDraftId("123"));
}
