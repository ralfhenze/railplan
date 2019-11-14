package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocation;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GeoLocationTest {

    @ParameterizedTest
    @CsvSource({
        // invalid coordinates
        "90.0001, 0.0",
        "-90.0001, 0.0",
        "0.0, 180.0001",
        "0.0, -180.0001",
    })
    void should_fail_on_invalid_Coordinates(Double latitude, Double longitude) {
        assertThrows(Exception.class, () -> {
            new GeoLocation(latitude, longitude);
        });
    }

    @ParameterizedTest
    @CsvSource({
        // valid coordinates
        "90.0, 0.0",
        "-90.0, 0.0",
        "0.0, 180.0",
        "0.0, -180.0",
    })
    void should_provide_Latitude_and_Longitude(Double latitude, Double longitude) {
        GeoLocation location = new GeoLocation(latitude, longitude);

        assertEquals(location.getLatitude(), latitude);
        assertEquals(location.getLongitude(), longitude);
    }

    @Test
    void should_calculate_the_Distance_between_two_Locations() {
        GeoLocation berlin = new GeoLocation(52.518611, 13.408333);
        GeoLocation hamburg = new GeoLocation(53.550556, 9.993333);

        double distance = berlin.getKilometerDistanceTo(hamburg);

        assertEquals(Math.floor(distance), 255);
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
        assertThrows(Exception.class, () -> {
            new GeoLocationInGermany(new GeoLocation(latitude, longitude));
        });
    }
}
