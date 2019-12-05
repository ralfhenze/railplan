package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RailwayTrackUT {

    @Test
    void should_be_equal_when_connecting_the_same_stations() {
        final var stationId1 = new TrainStationId("1");
        final var stationId2 = new TrainStationId("2");
        final var track1 = new RailwayTrack(stationId1, stationId2);
        final var track2 = new RailwayTrack(stationId1, stationId2);
        final var track3 = new RailwayTrack(stationId2, stationId1);

        assertThat(track1).isEqualTo(track2);
        assertThat(track2).isEqualTo(track3);
    }

    @Test
    void should_not_allow_to_connect_a_station_with_itself() {
        final var stationId = new TrainStationId("1");

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new RailwayTrack(stationId, stationId)
        );
    }
}
