package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

public class TestData {

    public static final String BERLIN_HBF_NAME = "Berlin Hbf";
    public static final double BERLIN_HBF_LAT = 52.524927;
    public static final double BERLIN_HBF_LNG = 13.369348;

    public static final String HAMBURG_HBF_NAME = "Hamburg Hbf";
    public static final double HAMBURG_HBF_LAT = 53.552596;
    public static final double HAMBURG_HBF_LNG = 10.006727;

    public static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );
}
