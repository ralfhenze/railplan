package com.ralfhenze.rms.railnetworkplanning.domain;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * MODIFIABLE
 *
 * [x] the maximum length of a Track is 200 km
 * [x] the minimum distance between two Stations is 10 km
 *     -> addStation(), moveStation()
 * [x] a Station's Name is unique
 *     -> addStation(), renameStation()
 * [x] two Stations can only be connected by a single Track
 *     -> connectStations()
 */
class RailNetworkDraft {
    RailNetworkDraftId id;
    Map<StationId, TrainStation> stations;
    // This alternative would enforce name uniqueness:
    Map<StationName, TrainStation> stations2;
    Set<DoubleTrackRailway> connections;

    StationId addStation(StationName name, GeoLocationInGermany location) {
        return new StationId();
    }

    // maybe make immutable and return new RailNetworkDraft instead of void
    void renameStation(StationId id, StationName name) {
    }

    void moveStation(StationId id, GeoLocationInGermany location) {
    }

    void deleteStation(StationId id) {
    }

    void connectStations(StationId id1, StationId id2) {
    }

    void disconnectStations(StationId id1, StationId id2) {
    }

    Optional<RailNetworkProposal> propose() {
        return Optional.empty();
    }
}
