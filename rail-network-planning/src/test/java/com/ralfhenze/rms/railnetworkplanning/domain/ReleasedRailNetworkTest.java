package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReleasedRailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(defaultPeriod, emptySet(), emptySet());
        });
    }

    @Test
    void should_ensure_at_least_one_track() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(defaultPeriod, setOf(berlinHbf, hamburgHbf), emptySet());
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                setOf(berlinHbf, hamburgHbf, frankfurtHbf, stuttgartHbf),
                setOf(
                    new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()),
                    new RailwayTrack(frankfurtHbf.getId(), stuttgartHbf.getId())
                )
            );
        });
    }

    @Test
    void should_ensure_no_standalone_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                setOf(berlinHbf, hamburgHbf, frankfurtHbf),
                setOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                setOf(berlinHbf, stuttgartHbf),
                setOf(new RailwayTrack(berlinHbf.getId(), stuttgartHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                setOf(berlinHbf, hamburgHbf.withName(new TrainStationName("Berlin Hbf"))),
                setOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                setOf(berlinHbf, berlinOst, hamburgHbf),
                setOf(
                    new RailwayTrack(berlinHbf.getId(), berlinOst.getId()),
                    new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId())
                )
            );
        });
    }

    @Test
    void should_ensure_no_duplicate_tracks() {
        final ReleasedRailNetwork railNetwork = new ReleasedRailNetwork(
            defaultPeriod,
            setOf(berlinHbf, hamburgHbf),
            setOf(
                new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()),
                new RailwayTrack(hamburgHbf.getId(), berlinHbf.getId())
            )
        );

        assertEquals(1, railNetwork.getTracks().size());
    }

    private <T> ImmutableSet<T> setOf(T... elements) {
        return Sets.immutable.of(elements);
    }

    private <T> ImmutableSet<T> emptySet() {
        return Sets.immutable.empty();
    }
}
