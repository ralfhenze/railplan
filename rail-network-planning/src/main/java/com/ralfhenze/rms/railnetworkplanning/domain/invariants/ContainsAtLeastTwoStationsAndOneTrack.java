package com.ralfhenze.rms.railnetworkplanning.domain.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.DoubleTrackRailway;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.Set;

public class ContainsAtLeastTwoStationsAndOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        ensureAtLeastTwoStations(stations);
        ensureAtLeastOneTrack(connections);
    }

    private void ensureAtLeastTwoStations(Set<TrainStation> stations) {
        if (stations.size() < 2) {
            throw new IllegalArgumentException(
                "At least 2 stations are required, but got " + stations.size()
            );
        }
    }

    private void ensureAtLeastOneTrack(Set<DoubleTrackRailway> connections) {
        if (connections.size() < 1) {
            throw new IllegalArgumentException(
                "At least 1 connection is required, but got " + connections.size()
            );
        }
    }
}
