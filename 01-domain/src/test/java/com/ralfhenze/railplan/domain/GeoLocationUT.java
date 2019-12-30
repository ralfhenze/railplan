package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void isInvalidWithCoordinatesOutsideOfGermany(Double latitude, Double longitude) {
        final var invalidLocation = new GeoLocationInGermany(latitude, longitude);
        assertThat(invalidLocation.isValid()).isFalse();
    }

    @Test
    public void providesLatitudeAndLongitudeAsString() {
        final var berlin = new GeoLocationInGermany(52.518611, 13.408333);

        assertThat(berlin.getLatitudeAsString()).isEqualTo("52.518611");
        assertThat(berlin.getLongitudeAsString()).isEqualTo("13.408333");
    }
}
