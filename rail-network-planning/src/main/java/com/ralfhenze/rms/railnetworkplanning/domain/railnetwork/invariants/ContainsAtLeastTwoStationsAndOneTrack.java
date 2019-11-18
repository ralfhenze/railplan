package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;

import java.util.Set;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class ContainsAtLeastTwoStationsAndOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<RailwayTrack> connections) {
        ensureNotNull(stations, "Stations");
        ensureNotNull(connections, "Connections");

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

    private void ensureAtLeastOneTrack(Set<RailwayTrack> connections) {
        if (connections.size() < 1) {
            throw new IllegalArgumentException(
                "At least 1 connection is required, but got " + connections.size()
            );
        }
    }
}