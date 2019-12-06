package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TrainStationNameUT {

    /*
    @ParameterizedTest
    @ValueSource(strings = {
        // invalid station names
        "berlin", // first lower case letter
        "Berlin\nHbf", // must not contain line breaks
        "Ber", // too short
        "This is a very very very very very long station name", // too long
    })
     */
    public void cannotBeConstructedWithAnInvalidStationName(String stationName) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new TrainStationName(stationName)
        );
    }

    /*
    @ParameterizedTest
    @ValueSource(strings = {
        // valid station names
        "Berlin Hbf",
        "München Hbf",
        "Frankfurt a.M. Hbf",
        "Halle (Saale) Hbf",
        "Kassel-Wilhelmshöhe",
    })
     */
    public void providesStationName(String stationName) {
        final var name = new TrainStationName(stationName);

        assertThat(name.getName()).isEqualTo(stationName);
    }
}
