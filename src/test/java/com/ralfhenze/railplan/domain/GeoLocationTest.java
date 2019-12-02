package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class GeoLocationTest {

    @Test
    void should_calculate_the_Distance_between_two_Locations() {
        final var berlin = new GeoLocationInGermany(52.518611, 13.408333);
        final var hamburg = new GeoLocationInGermany(53.550556, 9.993333);

        double distance = berlin.getKilometerDistanceTo(hamburg);

        assertThat(Math.floor(distance)).isEqualTo(255);
    }

    @ParameterizedTest
    @CsvSource({
        // coordinates outside of Germany
        "51.507460, -0.127607", // London
        "60.170721, 24.940860", // Helsinki
        "40.709212, -74.007229", // New York
        "-33.863272, 151.211579", // Sydney
    })
    void should_fail_on_Coordinates_outside_of_Germany(Double latitude, Double longitude) {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new GeoLocationInGermany(latitude, longitude)
        );
    }
}
