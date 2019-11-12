package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleTrackRailwayTest {

    @Test
    void should_be_equal_when_connecting_the_same_stations() {
        StationId stationId1 = new StationId("1");
        StationId stationId2 = new StationId("2");
        DoubleTrackRailway track1 = new DoubleTrackRailway(stationId1, stationId2);
        DoubleTrackRailway track2 = new DoubleTrackRailway(stationId1, stationId2);
        DoubleTrackRailway track3 = new DoubleTrackRailway(stationId2, stationId1);

        assertEquals(track1, track2);
        assertEquals(track2, track3);
    }
}
