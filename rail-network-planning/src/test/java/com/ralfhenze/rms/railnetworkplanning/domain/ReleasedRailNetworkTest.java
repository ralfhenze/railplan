package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReleasedRailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(defaultPeriod, emptyList(), emptyList());
        });
    }

    @Test
    void should_ensure_at_least_one_track() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(defaultPeriod, listOf(berlinHbf, hamburgHbf), emptyList());
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, hamburgHbf, frankfurtHbf, stuttgartHbf),
                listOf(
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
                listOf(berlinHbf, hamburgHbf, frankfurtHbf),
                listOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, stuttgartHbf),
                listOf(new RailwayTrack(berlinHbf.getId(), stuttgartHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, hamburgHbf.withName(new TrainStationName("Berlin Hbf"))),
                listOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, berlinOst, hamburgHbf),
                listOf(
                    new RailwayTrack(berlinHbf.getId(), berlinOst.getId()),
                    new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId())
                )
            );
        });
    }

    @Test
    void should_ensure_no_duplicate_tracks() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, hamburgHbf),
                listOf(
                    new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()),
                    new RailwayTrack(hamburgHbf.getId(), berlinHbf.getId())
                )
            );
        });
    }

    private <T> ImmutableList<T> listOf(T... elements) {
        return Lists.immutable.of(elements);
    }

    private <T> ImmutableList<T> emptyList() {
        return Lists.immutable.empty();
    }
}
