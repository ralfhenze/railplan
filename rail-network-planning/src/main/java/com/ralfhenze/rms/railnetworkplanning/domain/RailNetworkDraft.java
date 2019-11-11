package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;

import java.util.*;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotNull;

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
class RailNetworkDraft implements Aggregate {

    private final RailNetworkDraftId id;
    private final Map<StationId, TrainStation> stations = new HashMap<>();
    private final Set<DoubleTrackRailway> connections = new HashSet<>();

    RailNetworkDraft(final RailNetworkDraftId id) {
        assertNotNull(id, "Id is required");
        this.id = id;
    }

    public StationId addStation(final StationName name, final GeoLocationInGermany location) {
        return new StationId(null);
    }

    // maybe make immutable and return new RailNetworkDraft instead of void
    public void renameStation(final StationId id, final StationName name) {
    }

    public void moveStation(final StationId id, final GeoLocationInGermany location) {
    }

    public void deleteStation(final StationId id) {
    }

    public void connectStations(final StationId id1, final StationId id2) {
    }

    public void disconnectStations(final StationId id1, final StationId id2) {
    }

    Optional<RailNetworkProposal> propose() {
        return Optional.empty();
    }

    public Set<TrainStation> getStations() {
        return new HashSet<>(stations.values());
    }

    public Set<DoubleTrackRailway> getConnections() {
        return new HashSet<>(connections);
    }
}
