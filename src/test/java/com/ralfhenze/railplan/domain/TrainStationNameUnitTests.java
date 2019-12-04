package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TrainStationNameUnitTests {

    @ParameterizedTest
    @ValueSource(strings = {
        // invalid station names
        "berlin", // first lower case letter
        "Berlin\nHbf", // must not contain line breaks
        "Ber", // too short
        "This is a very very very very very long station name", // too long
    })
    void should_fail_on_invalid_station_names(String stationName) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new TrainStationName(stationName)
        );
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
        final var name = new TrainStationName(stationName);

        assertThat(name.getName()).isEqualTo(stationName);
    }
}
