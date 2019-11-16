package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
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
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf)),
                new HashSet<>()
            );
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf, frankfurtHbf, stuttgartHbf)),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()),
                    new DoubleTrackRailway(frankfurtHbf.getId(), stuttgartHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_no_standalone_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf, frankfurtHbf)),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, stuttgartHbf)),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), stuttgartHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new HashSet<>(Arrays.asList(berlinHbf, hamburgHbf.withName(new StationName("Berlin Hbf")))),
                new HashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new LinkedHashSet<>(Arrays.asList(berlinHbf, berlinOst, hamburgHbf)),
                new LinkedHashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), berlinOst.getId()),
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId())
                ))
            );
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                new LinkedHashSet<>(Arrays.asList(berlinHbf, hamburgHbf)),
                new LinkedHashSet<>(Arrays.asList(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()),
                    new DoubleTrackRailway(hamburgHbf.getId(), berlinHbf.getId())
                ))
            );
        });
    }
}
