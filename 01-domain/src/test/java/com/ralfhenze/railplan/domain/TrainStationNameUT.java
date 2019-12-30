package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void isInvalidWithAnInvalidStationName(String stationName) {
        assertThat(new TrainStationName(stationName).isValid()).isFalse();
    }

    @Test
    public void isInvalidWithTooShortStationName() {
        final var tooShortStationName = new TrainStationName("Ber");

        assertThat(tooShortStationName.isValid()).isFalse();
        assertThat(tooShortStationName.getValidationErrors()).hasSize(2);
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
