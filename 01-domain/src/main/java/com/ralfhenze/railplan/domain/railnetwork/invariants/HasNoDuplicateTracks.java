package com.ralfhenze.railplan.domain.railnetwork.invariants;

import com.ralfhenze.railplan.domain.common.Combinations;
import com.ralfhenze.railplan.domain.common.validation.ErrorMessage;
import com.ralfhenze.railplan.domain.common.validation.ValidationConstraint;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

public class HasNoDuplicateTracks
    implements ValidationConstraint<ImmutableList<RailwayTrack>> {

    private final Combinations combinations = new Combinations();

    private final ImmutableList<TrainStation> stations;

    public HasNoDuplicateTracks(
        final ImmutableList<TrainStation> stations
    ) {
        this.stations = stations;
    }

    @Override
    public Optional<ErrorMessage> validate(
        final ImmutableList<RailwayTrack> tracks,
        final String fieldName
    ) {
        if (tracks.size() < 2) {
            return Optional.empty();
        }

        final var equalTrackCombination = combinations
            .getUniqueCombinations(tracks)
            .detectOptional(stationCombination ->
                stationCombination
                    .getValue0()
                    .equals(stationCombination.getValue1())
                );

        if (equalTrackCombination.isPresent()) {
            final var track = equalTrackCombination.get().getValue0();
            final var station1 = stations
                .detect(s -> s.getId().equals(track.getFirstStationId()));
            final var station2 = stations
                .detect(s -> s.getId().equals(track.getSecondStationId()));

            return Optional.of(new ErrorMessage(
                "Track between \""
                    + station1.getName() + "\" and \""
                    + station2.getName() + "\" already exists"
            ));
        }

        return Optional.empty();
    }
}
