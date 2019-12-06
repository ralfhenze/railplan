package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

import java.util.Optional;

public class HasNoTracksLongerThan300Km
    implements ValidationConstraint<ImmutableList<RailwayTrack>> {

    private static final int MAXIMUM_LENGTH_KM = 300;

    private final ImmutableList<TrainStation> stations;

    public HasNoTracksLongerThan300Km(final ImmutableList<TrainStation> stations) {
        this.stations = stations;
    }

    @Override
    public Optional<ErrorMessage> validate(
        final ImmutableList<RailwayTrack> tracks,
        final String fieldName
    ) {
        if (tracks.isEmpty()) {
            return Optional.empty();
        }

        final var stationsMap = stations
            .toMap(TrainStation::getId, ts -> ts).toImmutable();

        final var tooLongTrack = tracks
            .detectOptional(track -> getTrackLength(
                stationsMap,
                track.getFirstStationId(),
                track.getSecondStationId()
            ) > MAXIMUM_LENGTH_KM);

        if (tooLongTrack.isPresent()) {
            final var id1 = tooLongTrack.get().getFirstStationId();
            final var id2 = tooLongTrack.get().getSecondStationId();
            final var trackLength = getTrackLength(stationsMap, id1, id2);

            return Optional.of(new ErrorMessage(
                "Track from \""
                    + stationsMap.get(id1).getName() + "\" (" + id1 + ") to \""
                    + stationsMap.get(id2).getName() + "\" (" + id2 + ") must be shorter than "
                    + MAXIMUM_LENGTH_KM + " km"
                    + ", but was ~" + Math.round(trackLength) + " km"
            ));
        }

        return Optional.empty();
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
