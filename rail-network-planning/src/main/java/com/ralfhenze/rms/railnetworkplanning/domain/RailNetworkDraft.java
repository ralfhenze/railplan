package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

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
    private final RailNetworkGraph graph = new RailNetworkGraph();

    RailNetworkDraft(final RailNetworkDraftId id) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
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

    public RailNetworkGraph getGraph() {
        return graph;
    }
}
