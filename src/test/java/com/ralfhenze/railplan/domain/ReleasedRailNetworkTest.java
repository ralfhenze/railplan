package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;

import static com.ralfhenze.railplan.domain.TestData.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ReleasedRailNetworkTest {

    @Test
    void should_ensure_at_least_two_stations() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(defaultPeriod, emptyList(), emptyList());
        });
    }

    @Test
    void should_ensure_at_least_one_track() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(defaultPeriod, listOf(berlinHbf, hamburgHbf), emptyList());
        });
    }

    @Test
    void should_ensure_no_unconnected_sub_graphs() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
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
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, hamburgHbf, frankfurtHbf),
                listOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_max_track_length() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, stuttgartHbf),
                listOf(new RailwayTrack(berlinHbf.getId(), stuttgartHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_unique_station_names() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                defaultPeriod,
                listOf(berlinHbf, hamburgHbf.withName(new TrainStationName("Berlin Hbf"))),
                listOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
            );
        });
    }

    @Test
    void should_ensure_minimum_station_distance() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
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
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
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
