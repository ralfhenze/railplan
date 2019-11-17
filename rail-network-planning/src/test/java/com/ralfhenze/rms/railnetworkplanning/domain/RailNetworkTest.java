package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.*;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                emptySet(),
                emptySet()
            );
        });
    }

    @Test
    void should_ensure_at_least_one_connection() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                setOf(berlinHbf, hamburgHbf),
                emptySet()
            );
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                setOf(berlinHbf, hamburgHbf, frankfurtHbf, stuttgartHbf),
                setOf(
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()),
                    new DoubleTrackRailway(frankfurtHbf.getId(), stuttgartHbf.getId())
                )
            );
        });
    }

    @Test
    void should_ensure_no_standalone_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                setOf(berlinHbf, hamburgHbf, frankfurtHbf),
                setOf(new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                setOf(berlinHbf, stuttgartHbf),
                setOf(new DoubleTrackRailway(berlinHbf.getId(), stuttgartHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                setOf(berlinHbf, hamburgHbf.withName(new StationName("Berlin Hbf"))),
                setOf(new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                new RailNetworkId("1"),
                new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
                setOf(berlinHbf, berlinOst, hamburgHbf),
                setOf(
                    new DoubleTrackRailway(berlinHbf.getId(), berlinOst.getId()),
                    new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId())
                )
            );
        });
    }

    @Test
    void should_ensure_no_duplicate_connections() {
        final RailNetwork railNetwork = new RailNetwork(
            new RailNetworkId("1"),
            new RailNetworkPeriod(LocalDate.of(2019, 11, 14), LocalDate.of(2019, 11, 20)),
            setOf(berlinHbf, hamburgHbf),
            setOf(
                new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()),
                new DoubleTrackRailway(hamburgHbf.getId(), berlinHbf.getId()) // the same connection again
            )
        );

        assertEquals(1, railNetwork.getConnections().size());
    }

    private <T> ImmutableSet<T> setOf(T... elements) {
        return Sets.immutable.of(elements);
    }

    private <T> ImmutableSet<T> emptySet() {
        return Sets.immutable.empty();
    }
}
