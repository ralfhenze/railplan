package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Test;

import static com.ralfhenze.railplan.domain.TestData.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST;
import static com.ralfhenze.railplan.domain.TestData.DEFAULT_PERIOD;
import static com.ralfhenze.railplan.domain.TestData.FRANKFURT_HBF;
import static com.ralfhenze.railplan.domain.TestData.HAMBURG_HBF;
import static com.ralfhenze.railplan.domain.TestData.STUTTGART_HBF;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ReleasedRailNetworkUT {

    @Test
    public void ensuresAtLeastTwoStations() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new ReleasedRailNetwork(DEFAULT_PERIOD, emptyList(), emptyList())
        );
    }

    @Test
    public void ensuresAtLeastOneTrack() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new ReleasedRailNetwork(DEFAULT_PERIOD, listOf(BERLIN_HBF, HAMBURG_HBF), emptyList())
        );
    }

    @Test
    public void ensuresNoUnconnectedSubGraphs() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, HAMBURG_HBF, FRANKFURT_HBF, STUTTGART_HBF),
                listOf(
                    new RailwayTrack(BERLIN_HBF.getId(), HAMBURG_HBF.getId()),
                    new RailwayTrack(FRANKFURT_HBF.getId(), STUTTGART_HBF.getId())
                )
            )
        );
    }

    @Test
    public void ensuresNoStandaloneStations() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, HAMBURG_HBF, FRANKFURT_HBF),
                listOf(new RailwayTrack(BERLIN_HBF.getId(), HAMBURG_HBF.getId()))
            )
        );
    }

    @Test
    public void ensuresMaxTrackLength() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, STUTTGART_HBF),
                listOf(new RailwayTrack(BERLIN_HBF.getId(), STUTTGART_HBF.getId()))
            );
        });
    }

    @Test
    public void ensuresUniqueStationNames() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, HAMBURG_HBF.withName(new TrainStationName("Berlin Hbf"))),
                listOf(new RailwayTrack(BERLIN_HBF.getId(), HAMBURG_HBF.getId()))
            );
        });
    }

    @Test
    public void ensuresMinimumStationDistance() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, BERLIN_OST, HAMBURG_HBF),
                listOf(
                    new RailwayTrack(BERLIN_HBF.getId(), BERLIN_OST.getId()),
                    new RailwayTrack(BERLIN_HBF.getId(), HAMBURG_HBF.getId())
                )
            );
        });
    }

    @Test
    public void ensureNoDuplicateTracks() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, HAMBURG_HBF),
                listOf(
                    new RailwayTrack(BERLIN_HBF.getId(), HAMBURG_HBF.getId()),
                    new RailwayTrack(HAMBURG_HBF.getId(), BERLIN_HBF.getId())
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
