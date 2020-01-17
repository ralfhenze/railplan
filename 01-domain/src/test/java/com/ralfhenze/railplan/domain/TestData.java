package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

public class TestData {

    public static final double BERLIN_OST_LAT = 52.510784;
    public static final double BERLIN_OST_LNG = 13.434832;

    public static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );
}
