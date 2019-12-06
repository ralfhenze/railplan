package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class GeoLocationUT {

    @Test
    public void providesDistanceBetweenTwoLocations() {
        final var berlin = new GeoLocationInGermany(52.518611, 13.408333);
        final var hamburg = new GeoLocationInGermany(53.550556, 9.993333);

        double distance = berlin.getKilometerDistanceTo(hamburg);

        assertThat(Math.floor(distance)).isEqualTo(255);
    }

    /*
    @ParameterizedTest
    @CsvSource({
        // coordinates outside of Germany
        "51.507460, -0.127607", // London
        "60.170721, 24.940860", // Helsinki
        "40.709212, -74.007229", // New York
        "-33.863272, 151.211579", // Sydney
    })
     */
    public void cannotBeConstructedWithCoordinatesOutsideOfGermany(Double latitude, Double longitude) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new GeoLocationInGermany(latitude, longitude)
        );
    }
}
