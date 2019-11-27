package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainStationTest {

    @ParameterizedTest
    @ValueSource(strings = {
        // invalid station names
        "berlin", // first lower case letter
        "Berlin\nHbf", // must not contain line breaks
        "Ber", // too short
        "This is a very very very very very long station name", // too long
    })
    void should_fail_on_invalid_station_names(String stationName) {
        assertThrows(Exception.class, () -> {
            new TrainStationName(stationName);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // valid station names
        "Berlin Hbf",
        "München Hbf",
        "Frankfurt a.M. Hbf",
        "Halle (Saale) Hbf",
        "Kassel-Wilhelmshöhe",
    })
    void should_provide_station_name(String stationName) {
        TrainStationName name = new TrainStationName(stationName);

        assertEquals(name.getName(), stationName);
    }
}
