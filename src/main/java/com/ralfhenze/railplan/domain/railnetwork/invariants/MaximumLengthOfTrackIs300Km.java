package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

public class MaximumLengthOfTrackIs300Km implements Invariant {

    private static final int MAXIMUM_LENGTH_KM = 300;

    @Override
    public void ensureIsSatisfied(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        ensureNotNull(stations, "Train Stations");
        ensureNotNull(tracks, "Railway Tracks");

        if (!tracks.isEmpty()) {
            ensureMaximumTrackLength(stations, tracks);
        }
    }

    private void ensureMaximumTrackLength(
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        final ImmutableMap<TrainStationId, TrainStation> stationsMap = stations
            .toMap(TrainStation::getId, ts -> ts).toImmutable();

        final Optional<RailwayTrack> tooLongTrack = tracks
            .detectOptional(track -> getTrackLength(
                stationsMap,
                track.getFirstStationId(),
                track.getSecondStationId()
            ) > MAXIMUM_LENGTH_KM);

        if (tooLongTrack.isPresent()) {
            final TrainStationId id1 = tooLongTrack.get().getFirstStationId();
            final TrainStationId id2 = tooLongTrack.get().getSecondStationId();
            final double trackLength = getTrackLength(stationsMap, id1, id2);

            throw new IllegalArgumentException(
                "Track from \""
                    + stationsMap.get(id1).getName() + "\" (" + id1 + ") to \""
                    + stationsMap.get(id2).getName() + "\" (" + id2 + ") must be shorter than "
                    + MAXIMUM_LENGTH_KM + " km"
                    + ", but was ~" + Math.round(trackLength) + " km"
            );
        }
    }

    private double getTrackLength(
        final ImmutableMap<TrainStationId, TrainStation> stationsMap,
        final TrainStationId id1,
        final TrainStationId id2
    ) {
        return stationsMap
            .get(id1)
            .getLocation()
            .getKilometerDistanceTo(stationsMap.get(id2).getLocation());
    }
}