package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Test;

import static com.ralfhenze.railplan.domain.TestData.berlinHbf;
import static com.ralfhenze.railplan.domain.TestData.berlinOst;
import static com.ralfhenze.railplan.domain.TestData.defaultPeriod;
import static com.ralfhenze.railplan.domain.TestData.frankfurtHbf;
import static com.ralfhenze.railplan.domain.TestData.hamburgHbf;
import static com.ralfhenze.railplan.domain.TestData.stuttgartHbf;
import static org.assertj.core.api.Assertions.assertThat;

public class ReleasedRailNetworkUT {

    @Test
    public void ensuresAtLeastTwoStations() {
        final var network = new ReleasedRailNetwork(defaultPeriod, emptyList(), emptyList());

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensuresAtLeastOneTrack() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod, listOf(berlinHbf, hamburgHbf), emptyList()
        );

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensuresNoUnconnectedSubGraphs() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            listOf(berlinHbf, hamburgHbf, frankfurtHbf, stuttgartHbf),
            listOf(
                new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()),
                new RailwayTrack(frankfurtHbf.getId(), stuttgartHbf.getId())
            )
        );

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensuresNoStandaloneStations() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            listOf(berlinHbf, hamburgHbf, frankfurtHbf),
            listOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
        );

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensuresMaxTrackLength() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            listOf(berlinHbf, stuttgartHbf),
            listOf(new RailwayTrack(berlinHbf.getId(), stuttgartHbf.getId()))
        );

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensuresUniqueStationNames() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            listOf(berlinHbf, hamburgHbf.withName(new TrainStationName("Berlin Hbf"))),
            listOf(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
        );

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensuresMinimumStationDistance() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            listOf(berlinHbf, berlinOst, hamburgHbf),
            listOf(
                new RailwayTrack(berlinHbf.getId(), berlinOst.getId()),
                new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId())
            )
        );

        assertThat(network.isValid()).isFalse();
    }

    @Test
    public void ensureNoDuplicateTracks() {
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            listOf(berlinHbf, hamburgHbf),
            listOf(
                new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()),
                new RailwayTrack(hamburgHbf.getId(), berlinHbf.getId())
            )
        );

        assertThat(network.isValid()).isFalse();
    }

    private <T> ImmutableList<T> listOf(T... elements) {
        return Lists.immutable.of(elements);
    }

    private <T> ImmutableList<T> emptyList() {
        return Lists.immutable.empty();
    }
}
