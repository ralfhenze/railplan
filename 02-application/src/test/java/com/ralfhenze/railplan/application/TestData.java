package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

public class TestData {

    public static final TrainStationName berlinHbfName = new TrainStationName("Berlin Hbf");
    public static final GeoLocationInGermany berlinHbfPos = new GeoLocationInGermany(52.524927, 13.369348);

    public static final TrainStationName hamburgHbfName = new TrainStationName("Hamburg Hbf");
    public static final GeoLocationInGermany hamburgHbfPos = new GeoLocationInGermany(53.552596, 10.006727);

    public static final ValidityPeriod defaultPeriod = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );
}
