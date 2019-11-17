package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.Invariant;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.DefaultRailNetworkInvariants;
import com.ralfhenze.rms.railnetworkplanning.domain.station.*;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;

import java.util.*;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the minimum distance between two Stations is 10 km
 * [x] a Station's Name is unique
 * [x] two Stations can only be connected by a single Track
 * [x] the maximum length of a Track is 300 km
 */
class RailNetworkDraft implements Aggregate {

    private final RailNetworkDraftId id;
    private final ImmutableSet<TrainStation> stations;
    private final ImmutableSet<DoubleTrackRailway> connections;
    private final ImmutableSet<Invariant> invariants = Sets.adapt(DefaultRailNetworkInvariants.INVARIANTS).toImmutable();
    private final int stationId;

    RailNetworkDraft(final RailNetworkDraftId id) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
        this.stations = Sets.immutable.empty();
        this.connections = Sets.immutable.empty();
        this.stationId = 1;
    }

    private RailNetworkDraft(
        final RailNetworkDraftId id,
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<DoubleTrackRailway> connections,
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
        final ImmutableSet<TrainStation> newStations = stations.newWith(addedStation);

        ensureInvariants(newStations, connections);

        return new RailNetworkDraft(this.id, newStations, this.connections, this.stationId + 1);
    }

    public RailNetworkDraft withRenamedStation(final StationName currentName, final StationName newName) {
        return withRenamedStation(getStationIdOf(currentName), newName);
    }

    public RailNetworkDraft withRenamedStation(final StationId id, final StationName name) {
        ensureStationIdExist(id);

        final TrainStation renamedStation = stations
            .detect(ts -> ts.getId().equals(id))
            .withName(name);
        final ImmutableSet<TrainStation> newStations = stations
            .reject(ts -> ts.getId().equals(id))
            .newWith(renamedStation);

        ensureInvariants(newStations, connections);

        return new RailNetworkDraft(this.id, newStations, this.connections, this.stationId);
    }

    public RailNetworkDraft withMovedStation(final StationName name, final GeoLocationInGermany location) {
        return withMovedStation(getStationIdOf(name), location);
    }

    public RailNetworkDraft withMovedStation(final StationId id, final GeoLocationInGermany location) {
        ensureStationIdExist(id);

        final TrainStation movedStation = stations
            .detect(ts -> ts.getId().equals(id))
            .withLocation(location);
        final ImmutableSet<TrainStation> newStations = stations
            .reject(ts -> ts.getId().equals(id))
            .newWith(movedStation);

        ensureInvariants(newStations, connections);

        return new RailNetworkDraft(this.id, newStations, this.connections, this.stationId);
    }

    public RailNetworkDraft withoutStation(final StationName name) {
        return withoutStation(getStationIdOf(name));
    }

    public RailNetworkDraft withoutStation(final StationId id) {
        return new RailNetworkDraft(
            this.id,
            this.stations.reject(ts -> ts.getId().equals(id)),
            this.connections.reject(track -> track.connectsStation(id)),
            this.stationId
        );
    }

    public RailNetworkDraft withConnection(final StationName name1, final StationName name2) {
        return withConnection(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withConnection(final StationId id1, final StationId id2) {
        ensureStationIdExist(id1);
        ensureStationIdExist(id2);

        final ImmutableSet<DoubleTrackRailway> newConnections = connections
            .newWith(new DoubleTrackRailway(id1, id2));

        ensureInvariants(stations, newConnections);

        return new RailNetworkDraft(this.id, this.stations, newConnections, this.stationId);
    }

    public RailNetworkDraft withoutConnection(final StationName name1, final StationName name2) {
        return withoutConnection(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withoutConnection(final StationId id1, final StationId id2) {
        final DoubleTrackRailway connection = new DoubleTrackRailway(id1, id2);

        return new RailNetworkDraft(
            this.id,
            this.stations,
            this.connections.reject(track -> track.equals(connection)),
            this.stationId
        );
    }

    public ImmutableSet<TrainStation> getStations() {
        return stations;
    }

    public ImmutableSet<DoubleTrackRailway> getConnections() {
        return connections;
    }

    private StationId getStationIdOf(final StationName name) {
        return stations
            .detect(ts -> ts.getName().equals(name))
            .getId();
    }

    private void ensureInvariants(
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<DoubleTrackRailway> connections
    ) {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations.castToSet(), connections.castToSet());
        }
    }

    private void ensureStationIdExist(final StationId stationId) {
        if (stations.noneSatisfy(ts -> ts.getId().equals(stationId))) {
            throw new IllegalArgumentException(
                "Station ID \"" + stationId + "\" does not exist"
            );
        }
    }
}
