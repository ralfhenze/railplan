package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(JUnitParamsRunner.class)
public class TrainStationNameUT {

    @Test
    @Parameters({
        // invalid station names
        "berlin", // first lower case letter
        "Berlin\nHbf", // must not contain line breaks
        "Ber", // too short
        "This is a very very very very very long station name", // too long
    })
    public void cannotBeConstructedWithAnInvalidStationName(String stationName) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new TrainStationName(stationName)
        );
    }

    @Test
    @Parameters({
        // valid station names
        "Berlin Hbf",
        "München Hbf",
        "Frankfurt a.M. Hbf",
        "Halle (Saale) Hbf",
        "Kassel-Wilhelmshöhe",
    })
    public void providesStationName(String stationName) {
        final var name = new TrainStationName(stationName);

        assertThat(name.getName()).isEqualTo(stationName);
    }
}
