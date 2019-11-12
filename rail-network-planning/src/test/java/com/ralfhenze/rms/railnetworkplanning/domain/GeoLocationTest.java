package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GeoLocationTest {

    @ParameterizedTest
    @CsvSource({
        // invalid locations
        "90.0001, 0.0",
        "-90.0001, 0.0",
        "0.0, 180.0001",
        "0.0, -180.0001",
    })
    void it_should_not_be_possible_to_create_invalid_GeoLocations(Double latitude, Double longitude) {
        assertThrows(Exception.class, () -> {
            new GeoLocation(latitude, longitude);
        });
    }

    @ParameterizedTest
    @CsvSource({
        // valid locations
        "90.0, 0.0",
        "-90.0, 0.0",
        "0.0, 180.0",
        "0.0, -180.0",
    })
    void it_should_be_possible_to_create_valid_GeoLocations(Double latitude, Double longitude) {
        GeoLocation location = new GeoLocation(latitude, longitude);

        assertEquals(location.getLatitude(), latitude);
        assertEquals(location.getLongitude(), longitude);
    }
}
