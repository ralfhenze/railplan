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
    private final Map<StationId, TrainStation> stations;
    private final Set<DoubleTrackRailway> connections;
    private final Set<Invariant> invariants = DefaultRailNetworkInvariants.INVARIANTS;
    private final int stationId;

    RailNetworkDraft(final RailNetworkDraftId id) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
        this.stations = new HashMap<>();
        this.connections = new HashSet<>();
        this.stationId = 1;
    }

    private RailNetworkDraft(
        final RailNetworkDraftId id,
        final Map<StationId, TrainStation> stations,
        final Set<DoubleTrackRailway> connections,
        final int stationId
    ) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
        this.stations = stations;
        this.connections = connections;
        this.stationId = stationId;
    }

    public RailNetworkDraft withNewStation(final StationName name, final GeoLocationInGermany location) {
        final StationId stationId = new StationId(String.valueOf(this.stationId));
        final TrainStation addedStation = new TrainStation(stationId, name, location);
        final Set<TrainStation> newStations = new LinkedHashSet<>(stations.values());
        newStations.add(addedStation);

        ensureInvariants(newStations, connections);

        final Map<StationId, TrainStation> newStationsMap = new HashMap<>(this.stations);
        newStationsMap.put(stationId, addedStation);

        return new RailNetworkDraft(this.id, newStationsMap, this.connections, this.stationId + 1);
    }

    public RailNetworkDraft withRenamedStation(final StationName currentName, final StationName newName) {
        return withRenamedStation(getStationIdOf(currentName), newName);
    }

    public RailNetworkDraft withRenamedStation(final StationId id, final StationName name) {
        ensureStationIdExist(id);

        final TrainStation renamedStation = stations.get(id).withName(name);
        final Set<TrainStation> newStations = stations.values().stream()
            .filter(ts -> !ts.getId().equals(id))
            .collect(Collectors.toSet());
        newStations.add(renamedStation);

        ensureInvariants(newStations, connections);

        final Map<StationId, TrainStation> newStationsMap = new HashMap<>(this.stations);
        newStationsMap.replace(id, renamedStation);

        return new RailNetworkDraft(this.id, newStationsMap, this.connections, this.stationId);
    }

    public RailNetworkDraft withMovedStation(final StationName name, final GeoLocationInGermany location) {
        return withMovedStation(getStationIdOf(name), location);
    }

    public RailNetworkDraft withMovedStation(final StationId id, final GeoLocationInGermany location) {
        ensureStationIdExist(id);

        final TrainStation movedStation = stations.get(id).withLocation(location);
        final Set<TrainStation> newStations = stations.values().stream()
            .filter(ts -> !ts.getId().equals(id))
            .collect(Collectors.toSet());
        newStations.add(movedStation);

        ensureInvariants(newStations, connections);

        final Map<StationId, TrainStation> newStationsMap = new HashMap<>(this.stations);
        newStationsMap.replace(id, movedStation);

        return new RailNetworkDraft(this.id, newStationsMap, this.connections, this.stationId);
    }

    public RailNetworkDraft withoutStation(final StationName name) {
        return withoutStation(getStationIdOf(name));
    }

    public RailNetworkDraft withoutStation(final StationId id) {
        final Set<DoubleTrackRailway> newConnections = new LinkedHashSet<>(connections);
        newConnections.removeIf(track -> track.connectsStation(id));

        final Map<StationId, TrainStation> newStationsMap = new HashMap<>(this.stations);
        newStationsMap.remove(id);

        return new RailNetworkDraft(this.id, newStationsMap, newConnections, this.stationId);
    }

    public RailNetworkDraft withConnection(final StationName name1, final StationName name2) {
        return withConnection(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withConnection(final StationId id1, final StationId id2) {
        ensureStationIdExist(id1);
        ensureStationIdExist(id2);

        final DoubleTrackRailway addedConnection = new DoubleTrackRailway(id1, id2);
        final Set<DoubleTrackRailway> newConnections = new LinkedHashSet<>(connections);
        newConnections.add(addedConnection);

        ensureInvariants(new LinkedHashSet<>(stations.values()), newConnections);

        return new RailNetworkDraft(this.id, this.stations, newConnections, this.stationId);
    }

    public RailNetworkDraft withoutConnection(final StationName name1, final StationName name2) {
        return withoutConnection(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withoutConnection(final StationId id1, final StationId id2) {
        DoubleTrackRailway connection = new DoubleTrackRailway(id1, id2);
        final Set<DoubleTrackRailway> newConnections = new LinkedHashSet<>(connections);
        newConnections.removeIf(track -> track.equals(connection));

        return new RailNetworkDraft(this.id, this.stations, newConnections, this.stationId);
    }

    public Set<TrainStation> getStations() {
        return new LinkedHashSet<>(stations.values());
    }

    public Set<DoubleTrackRailway> getConnections() {
        return connections;
    }

    private StationId getStationIdOf(final StationName name) {
        Optional<TrainStation> station = stations.values().stream()
            .filter(ts -> ts.getName().equals(name))
            .findAny();

        return station.get().getId();
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
