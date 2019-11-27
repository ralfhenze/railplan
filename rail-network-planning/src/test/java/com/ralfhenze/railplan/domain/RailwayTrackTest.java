package com.ralfhenze.railplan.domain;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RailwayTrackTest {

    @Test
    void should_be_equal_when_connecting_the_same_stations() {
        TrainStationId stationId1 = new TrainStationId("1");
        TrainStationId stationId2 = new TrainStationId("2");
        RailwayTrack track1 = new RailwayTrack(stationId1, stationId2);
        RailwayTrack track2 = new RailwayTrack(stationId1, stationId2);
        RailwayTrack track3 = new RailwayTrack(stationId2, stationId1);

        assertEquals(track1, track2);
        assertEquals(track2, track3);
    }

    @Test
    void should_not_allow_to_connect_a_station_with_itself() {
        TrainStationId stationId1 = new TrainStationId("1");

        assertThrows(Exception.class, () -> {
            new RailwayTrack(stationId1, stationId1);
        });
    }
}
