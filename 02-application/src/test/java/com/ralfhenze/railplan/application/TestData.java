package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

public class TestData {

    public static final String berlinHbfName = "Berlin Hbf";
    public static final double berlinHbfLat = 52.524927;
    public static final double berlinHbfLng = 13.369348;

    public static final String hamburgHbfName = "Hamburg Hbf";
    public static final double hamburgHbfLat = 53.552596;
    public static final double hamburgHbfLng = 10.006727;

    public static final ValidityPeriod defaultPeriod = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );
}
