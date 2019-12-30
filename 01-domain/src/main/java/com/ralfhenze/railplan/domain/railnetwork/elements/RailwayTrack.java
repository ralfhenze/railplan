package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.Validatable;
import com.ralfhenze.railplan.domain.common.ValueObject;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotEqualTo;

import java.util.List;

/**
 * https://en.wikipedia.org/wiki/Double-track_railway
 *
 * [x] a Track connects two different Stations
 * [x] a Track has no direction
 * [x] equal when two DoubleTrackRailways connect the same Stations
 */
public class RailwayTrack implements ValueObject, Validatable {

    private final TrainStationId firstStationId;
    private final TrainStationId secondStationId;

    public RailwayTrack(
        final TrainStationId firstStationId,
        final TrainStationId secondStationId
    ) {
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
    }

    @Override
    public boolean isValid() {
        return getFirstStationIdErrors().isEmpty()
            && getSecondStationIdErrors().isEmpty();
    }

    public List<ValidationError> getFirstStationIdErrors() {
        return List.of();
    }

    public List<ValidationError> getSecondStationIdErrors() {
        return new Validation<>(secondStationId)
            .ensureIt(new IsNotEqualTo<>(firstStationId))
            .getValidationErrors();
    }

    public boolean connectsStation(final TrainStationId stationId) {
        return firstStationId.equals(stationId) || secondStationId.equals(stationId);
    }

    public TrainStationId getFirstStationId() {
        return firstStationId;
    }

    public TrainStationId getSecondStationId() {
        return secondStationId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RailwayTrack)) {
            return false;
        }

        final var track = (RailwayTrack)object;

        return (
            (track.firstStationId.equals(this.firstStationId)
                && track.secondStationId.equals(this.secondStationId))
            ||
            (track.firstStationId.equals(this.secondStationId)
                && track.secondStationId.equals(this.firstStationId))
        );
    }
}
