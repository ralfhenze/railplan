package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;

import java.time.LocalDate;

public class TestData {

    public static final String BERLIN_OST_NAME = "Berlin Ostbahnhof";
    public static final double BERLIN_OST_LAT = 52.510784;
    public static final double BERLIN_OST_LNG = 13.434832;

    public static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

    public static RailNetworkDraft getDraftWith(PresetStation... stations) {
        var draft = new RailNetworkDraft();
        for (final var station : stations) {
            draft = draft.withNewStation(
                station.getName(),
                station.getLatitude(),
                station.getLongitude()
            );
        }

        return draft;
    }
}
