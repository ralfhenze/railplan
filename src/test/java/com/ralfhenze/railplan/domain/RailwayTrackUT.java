package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RailwayTrackUT {

    @Test
    void isEqualWhenConnectingTheSameStations() {
        final var stationId1 = new TrainStationId("1");
        final var stationId2 = new TrainStationId("2");
        final var track1 = new RailwayTrack(stationId1, stationId2);
        final var track2 = new RailwayTrack(stationId1, stationId2);
        final var track3 = new RailwayTrack(stationId2, stationId1);

        assertThat(track1).isEqualTo(track2);
        assertThat(track2).isEqualTo(track3);
    }

    @Test
    void cannotBeConstructedWithTheSameStationIds() {
        final var stationId = new TrainStationId("1");

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new RailwayTrack(stationId, stationId)
        );
    }
}
