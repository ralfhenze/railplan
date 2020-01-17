package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Test;

import java.time.LocalDate;

import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST_LAT;
import static com.ralfhenze.railplan.domain.TestData.BERLIN_OST_LNG;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ReleasedRailNetworkUT {

    private final static TrainStation BERLIN_HBF = getStation(1, PresetStation.BERLIN_HBF);
    private final static TrainStation HAMBURG_HBF = getStation(2, PresetStation.HAMBURG_HBF);
    private final static TrainStation FRANKFURT_HBF = getStation(3, PresetStation.FRANKFURT_HBF);
    private final static TrainStation STUTTGART_HBF = getStation(4, PresetStation.STUTTGART_HBF);
    private static final ValidityPeriod DEFAULT_PERIOD = new ValidityPeriod(
        LocalDate.of(2019, 11, 14),
        LocalDate.of(2019, 11, 20)
    );

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
        final var berlinOst = new TrainStation(
            new TrainStationId(5),
            new TrainStationName("Berlin Ostbahnhof"),
            new GeoLocationInGermany(BERLIN_OST_LAT, BERLIN_OST_LNG)
        );

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> {
            new ReleasedRailNetwork(
                DEFAULT_PERIOD,
                listOf(BERLIN_HBF, berlinOst, HAMBURG_HBF),
                listOf(
                    new RailwayTrack(BERLIN_HBF.getId(), berlinOst.getId()),
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

    private static TrainStation getStation(
        final int id,
        final PresetStation presetStation
    ) {
        return new TrainStation(
            new TrainStationId(id),
            new TrainStationName(presetStation.getName()),
            new GeoLocationInGermany(presetStation.getLatitude(), presetStation.getLongitude())
        );
    }
}
