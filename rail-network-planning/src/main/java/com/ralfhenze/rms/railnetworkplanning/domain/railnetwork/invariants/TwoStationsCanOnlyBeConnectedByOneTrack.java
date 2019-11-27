package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Combinations;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;
import org.javatuples.Pair;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class TwoStationsCanOnlyBeConnectedByOneTrack implements Invariant {

    private final Combinations combinations = new Combinations();

    @Override
    public void ensureIsSatisfied(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        ensureNotNull(stations, "Train Stations");
        ensureNotNull(tracks, "Railway Tracks");

        if (tracks.size() >= 2) {
            ensureNoDuplicateTracks(stations, tracks);
        }
    }

    private void ensureNoDuplicateTracks(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        final Optional<Pair<RailwayTrack, RailwayTrack>> equalTrackCombination = combinations
            .getUniqueCombinations(tracks)
            .detectOptional(stationCombination ->
                stationCombination
                    .getValue0()
                    .equals(stationCombination.getValue1())
                );

        if (equalTrackCombination.isPresent()) {
            final RailwayTrack track = equalTrackCombination.get().getValue0();
            final TrainStation station1 = stations
                .detect(s -> s.getId().equals(track.getFirstStationId()));
            final TrainStation station2 = stations
                .detect(s -> s.getId().equals(track.getSecondStationId()));

            throw new IllegalArgumentException(
                "Track between \""
                    + station1.getName() + "\" and \""
                    + station2.getName() + "\" already exists"
            );
        }
    }
}
