package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;
import com.ralfhenze.rms.railnetworkplanning.domain.station.TrainStation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * MODIFIABLE
 *
 * [ ] the maximum length of a Track is 200 km
 * [ ] the minimum distance between two Stations is 10 km
 *     -> addStation(), moveStation()
 * [x] a Station's Name is unique
 *     -> addStation(), renameStation()
 * [ ] two Stations can only be connected by a single Track
 *     -> connectStations()
 */
class RailNetworkDraft implements Aggregate {

    private final RailNetworkDraftId id;
    private final RailNetworkGraph graph = new RailNetworkGraph();

    private final Map<StationId, TrainStation> stations = new HashMap<>();
    private final Set<DoubleTrackRailway> connections = new HashSet<>();

    RailNetworkDraft(final RailNetworkDraftId id) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
    }

    public StationId addStation(final StationName name, final GeoLocationInGermany location) {
        ensureStationNameDoesNotExist(name);

        final StationId stationId = new StationId(String.valueOf(stations.size() + 1));
        stations.put(stationId, new TrainStation(stationId, name, location));

        return stationId;
    }

    public void renameStation(final StationId id, final StationName name) {
        ensureStationIdExist(id);
        ensureStationNameDoesNotExist(name);

        final TrainStation station = stations.get(id);

        stations.put(id, station.withName(name));
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

    private void ensureStationNameDoesNotExist(final StationName stationName) {
        final boolean nameExists = this.stations.values().stream()
            .anyMatch(station -> station.getName().equals(stationName));

        if (nameExists) {
            throw new IllegalArgumentException(
                "Station Name \"" + stationName.getName() + "\" already exists"
            );
        }
    }

    private void ensureStationIdExist(final StationId stationId) {
        if (!stations.containsKey(stationId)) {
            throw new IllegalArgumentException(
                "Station ID \"" + stationId + "\" does not exist"
            );
        }
    }
}
