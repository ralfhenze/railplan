package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStation;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

public class TwoStationsCanOnlyBeConnectedByOneTrack implements Invariant {

    @Override
    public void ensureIsSatisfied(Set<TrainStation> stations, Set<RailwayTrack> tracks) {
        ensureNotNull(stations, "Train Stations");
        ensureNotNull(tracks, "Railway Tracks");

        if (tracks.size() >= 2) {
            ensureNoDuplicateTracks(stations, tracks);
        }
    }

    private void ensureNoDuplicateTracks(Set<TrainStation> stations, Set<RailwayTrack> tracks) {

        final List<Pair<RailwayTrack, RailwayTrack>> uniqueTrackCombinations = new ArrayList<>();
        final Deque<RailwayTrack> sourceTracks = new LinkedList<>(tracks);
        final Deque<RailwayTrack> otherTracks = new LinkedList<>(tracks);

        sourceTracks.removeLast();

        for (final RailwayTrack sourceTrack : sourceTracks) {
            otherTracks.removeFirst();
            for (RailwayTrack otherTrack : otherTracks) {
                uniqueTrackCombinations.add(new Pair<>(sourceTrack, otherTrack));
            }
        }

        final Optional<Pair<RailwayTrack, RailwayTrack>> equalTrackCombination = uniqueTrackCombinations
            .stream()
            .filter(stationCombination ->
                stationCombination
                    .getValue0()
                    .equals(stationCombination.getValue1())
                )
            .findAny();

        if (equalTrackCombination.isPresent()) {
            final TrainStationId id1 = equalTrackCombination.get().getValue0().getFirstStationId();
            final TrainStationId id2 = equalTrackCombination.get().getValue0().getSecondStationId();
            final Map<TrainStationId, TrainStation> stationsMap = stations.stream()
                .collect(Collectors.toMap(TrainStation::getId, ts -> ts));

            throw new IllegalArgumentException(
                "Track between \""
                    + stationsMap.get(id1).getName() + "\" and \""
                    + stationsMap.get(id2).getName() + "\" already exists"
            );
        }
    }
}
