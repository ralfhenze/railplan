package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(),
                new HashSet<>()
            );
        });
    }

    @Test
    void should_ensure_at_least_one_connection() {
        TrainStation berlinHbf = new TrainStation(
            new StationId("1"),
            new StationName("Berlin Hbf"),
            new GeoLocationInGermany(new GeoLocation(52.524927, 13.369348))
        );
        TrainStation hamburgHbf = new TrainStation(
            new StationId("2"),
            new StationName("Hamburg Hbf"),
            new GeoLocationInGermany(new GeoLocation(53.552596, 10.006727))
        );

        assertThrows(Exception.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf)),
                new HashSet<>()
            );
        });
    }
}
