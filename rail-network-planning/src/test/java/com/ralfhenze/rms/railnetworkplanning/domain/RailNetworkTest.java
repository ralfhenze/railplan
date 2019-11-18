package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.StationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.RailNetwork;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(defaultPeriod, emptySet(), emptySet());
        });
    }

    @Test
    void should_ensure_at_least_one_connection() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(defaultPeriod, setOf(berlinHbf, hamburgHbf), emptySet());
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                defaultPeriod,
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
                defaultPeriod,
                setOf(berlinHbf, hamburgHbf, frankfurtHbf),
                setOf(new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                defaultPeriod,
                setOf(berlinHbf, stuttgartHbf),
                setOf(new DoubleTrackRailway(berlinHbf.getId(), stuttgartHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                defaultPeriod,
                setOf(berlinHbf, hamburgHbf.withName(new StationName("Berlin Hbf"))),
                setOf(new DoubleTrackRailway(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RailNetwork(
                defaultPeriod,
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
            defaultPeriod,
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
