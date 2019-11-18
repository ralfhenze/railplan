package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.set.ImmutableSet;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class ContainsAtLeastTwoStationsAndOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<RailwayTrack> tracks
    ) {
        ensureNotNull(stations, "Train Stations");
        ensureNotNull(tracks, "Railway Tracks");

        ensureAtLeastTwoStations(stations);
        ensureAtLeastOneTrack(tracks);
    }

    private void ensureAtLeastTwoStations(final ImmutableSet<TrainStation> stations) {
        if (stations.size() < 2) {
            throw new IllegalArgumentException(
                "At least 2 stations are required, but got " + stations.size()
            );
        }
    }

    private void ensureAtLeastOneTrack(final ImmutableSet<RailwayTrack> tracks) {
        if (tracks.size() < 1) {
            throw new IllegalArgumentException(
                "At least 1 track is required, but got " + tracks.size()
            );
        }
    }
}
