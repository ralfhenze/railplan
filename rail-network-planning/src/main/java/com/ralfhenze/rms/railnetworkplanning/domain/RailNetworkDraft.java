package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.Invariant;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.DefaultRailNetworkInvariants;
import com.ralfhenze.rms.railnetworkplanning.domain.station.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the minimum distance between two Stations is 10 km
 * [x] a Station's Name is unique
 * [x] two Stations can only be connected by a single Track
 * [x] the maximum length of a Track is 300 km
 */
class RailNetworkDraft implements Aggregate {

    private final RailNetworkDraftId id;

    private final Map<StationId, TrainStation> stations = new HashMap<>();
    private final Set<DoubleTrackRailway> connections = new HashSet<>();
    private final Set<Invariant> invariants = DefaultRailNetworkInvariants.INVARIANTS;

    private int stationId = 1;

    RailNetworkDraft(final RailNetworkDraftId id) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
    }

    public StationId addStation(final StationName name, final GeoLocationInGermany location) {
        final StationId stationId = new StationId(String.valueOf(this.stationId++));
        final TrainStation addedStation = new TrainStation(stationId, name, location);
        final Set<TrainStation> newStations = new LinkedHashSet<>(stations.values());
        newStations.add(addedStation);

        ensureInvariants(newStations, connections);

        stations.put(stationId, addedStation);

        return stationId;
    }

    public void renameStation(final StationId id, final StationName name) {
        ensureStationIdExist(id);

        final TrainStation renamedStation = stations.get(id).withName(name);
        final Set<TrainStation> newStations = stations.values().stream()
            .filter(ts -> !ts.getId().equals(id))
            .collect(Collectors.toSet());
        newStations.add(renamedStation);

        ensureInvariants(newStations, connections);

        stations.replace(id, renamedStation);
    }

    public void moveStation(final StationId id, final GeoLocationInGermany location) {
        ensureStationIdExist(id);

        final TrainStation movedStation = stations.get(id).withLocation(location);
        final Set<TrainStation> newStations = stations.values().stream()
            .filter(ts -> !ts.getId().equals(id))
            .collect(Collectors.toSet());
        newStations.add(movedStation);

        ensureInvariants(newStations, connections);

        stations.replace(id, movedStation);
    }

    public void deleteStation(final StationId id) {
        connections.removeIf(track -> track.connectsStation(id));
        stations.remove(id);
    }

    public void connectStations(final StationId id1, final StationId id2) {
        ensureStationIdExist(id1);
        ensureStationIdExist(id2);

        final DoubleTrackRailway addedConnection = new DoubleTrackRailway(id1, id2);
        final Set<DoubleTrackRailway> newConnections = new LinkedHashSet<>(connections);
        newConnections.add(addedConnection);

        ensureInvariants(new LinkedHashSet<>(stations.values()), newConnections);

        connections.add(addedConnection);
    }

    public void disconnectStations(final StationId id1, final StationId id2) {
        DoubleTrackRailway connection = new DoubleTrackRailway(id1, id2);
        connections.removeIf(track -> track.equals(connection));
    }

    public Set<TrainStation> getStations() {
        return new LinkedHashSet<>(stations.values());
    }

    public Set<DoubleTrackRailway> getConnections() {
        return connections;
    }

    private void ensureInvariants(Set<TrainStation> stations, Set<DoubleTrackRailway> connections) {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations, connections);
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
