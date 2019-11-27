package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.ValueObject;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotEqual;
import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Double-track_railway
 *
 * [x] a Track connects two different Stations
 * [x] a Track has no direction
 * [x] equal when two DoubleTrackRailways connect the same Stations
 */
public class RailwayTrack implements ValueObject {

    private final TrainStationId firstStationId;
    private final TrainStationId secondStationId;

    public RailwayTrack(final TrainStationId firstStationId, final TrainStationId secondStationId) {
        ensureNotEqual(firstStationId, secondStationId, "First Station ID", "Second Station ID");

        this.firstStationId = ensureNotNull(firstStationId, "First Station ID");
        this.secondStationId = ensureNotNull(secondStationId, "Second Station ID");
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

        RailwayTrack track = (RailwayTrack)object;

        return (
            (track.firstStationId.equals(this.firstStationId)
                && track.secondStationId.equals(this.secondStationId))
            ||
            (track.firstStationId.equals(this.secondStationId)
                && track.secondStationId.equals(this.firstStationId))
        );
    }
}
