package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.ValueObject;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Double-track_railway
 *
 * [ ] a Track connects two different Stations
 *     -> Smart Constructor
 * [x] a Track has no direction
 *     -> implementation
 *
 * [ ] equal when two DoubleTrackRailways connect the same Stations
 */
class DoubleTrackRailway implements ValueObject {

    private final StationId firstStationId;
    private final StationId secondStationId;

    DoubleTrackRailway(final StationId firstStationId, final StationId secondStationId) {
        this.firstStationId = ensureNotNull(firstStationId, "First Station ID");
        this.secondStationId = ensureNotNull(secondStationId, "Second Station ID");
    }

    public StationId getFirstStationId() {
        return firstStationId;
    }

    public StationId getSecondStationId() {
        return secondStationId;
    }
}
