package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotEqual;
import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Double-track_railway
 *
 * [x] a Track connects two different Stations
 * [x] a Track has no direction
 * [x] equal when two DoubleTrackRailways connect the same Stations
 */
public class DoubleTrackRailway implements ValueObject {

    private final StationId firstStationId;
    private final StationId secondStationId;

    public DoubleTrackRailway(final StationId firstStationId, final StationId secondStationId) {
        ensureNotEqual(firstStationId, secondStationId, "First Station ID", "Second Station ID");

        this.firstStationId = ensureNotNull(firstStationId, "First Station ID");
        this.secondStationId = ensureNotNull(secondStationId, "Second Station ID");
    }

    public boolean connectsStation(final StationId stationId) {
        return firstStationId.equals(stationId) || secondStationId.equals(stationId);
    }

    public StationId getFirstStationId() {
        return firstStationId;
    }

    public StationId getSecondStationId() {
        return secondStationId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DoubleTrackRailway)) {
            return false;
        }

        DoubleTrackRailway track = (DoubleTrackRailway)object;

        return (
            (track.firstStationId.equals(this.firstStationId)
                && track.secondStationId.equals(this.secondStationId))
            ||
            (track.firstStationId.equals(this.secondStationId)
                && track.secondStationId.equals(this.firstStationId))
        );
    }
}
