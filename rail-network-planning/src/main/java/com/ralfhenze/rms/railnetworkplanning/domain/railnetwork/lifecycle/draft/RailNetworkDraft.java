package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants.Invariant;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.invariants.DefaultRailNetworkInvariants;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.*;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the minimum distance between two Stations is 10 km
 * [x] a Station's Name is unique
 * [x] two Stations can only be connected by a single Track
 * [x] the maximum length of a Track is 300 km
 */
public class RailNetworkDraft implements Aggregate {

    private final Optional<RailNetworkDraftId> id;
    private final ImmutableSet<TrainStation> stations;
    private final ImmutableSet<RailwayTrack> connections;
    private final ImmutableSet<Invariant> invariants = DefaultRailNetworkInvariants.INVARIANTS;
    private final int stationId;

    public RailNetworkDraft() {
        this(
            Optional.empty(),
            Sets.immutable.empty(),
            Sets.immutable.empty(),
            1
        );
    }

    private RailNetworkDraft(
        final Optional<RailNetworkDraftId> id,
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<RailwayTrack> connections,
        final int stationId
    ) {
        this.id = ensureNotNull(id, "Rail Network Draft ID");
        this.stations = stations;
        this.connections = connections;
        this.stationId = stationId;
    }

    public RailNetworkDraft withId(RailNetworkDraftId id) {
        ensureNotNull(id, "Rail Network Draft ID");

        return new RailNetworkDraft(Optional.of(id), stations, connections, stationId);
    }

    public RailNetworkDraft withNewStation(final TrainStationName name, final GeoLocationInGermany location) {
        final TrainStationId stationId = new TrainStationId(String.valueOf(this.stationId));
        final TrainStation addedStation = new TrainStation(stationId, name, location);
        final ImmutableSet<TrainStation> newStations = stations.newWith(addedStation);

        ensureInvariants(newStations, connections);

        return new RailNetworkDraft(this.id, newStations, this.connections, this.stationId + 1);
    }

    public RailNetworkDraft withRenamedStation(final TrainStationName currentName, final TrainStationName newName) {
        return withRenamedStation(getStationIdOf(currentName), newName);
    }

    public RailNetworkDraft withRenamedStation(final TrainStationId id, final TrainStationName name) {
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

    public RailNetworkDraft withMovedStation(final TrainStationName name, final GeoLocationInGermany location) {
        return withMovedStation(getStationIdOf(name), location);
    }

    public RailNetworkDraft withMovedStation(final TrainStationId id, final GeoLocationInGermany location) {
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

    public RailNetworkDraft withoutStation(final TrainStationName name) {
        return withoutStation(getStationIdOf(name));
    }

    public RailNetworkDraft withoutStation(final TrainStationId id) {
        return new RailNetworkDraft(
            this.id,
            this.stations.reject(ts -> ts.getId().equals(id)),
            this.connections.reject(track -> track.connectsStation(id)),
            this.stationId
        );
    }

    public RailNetworkDraft withConnection(final TrainStationName name1, final TrainStationName name2) {
        return withConnection(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withConnection(final TrainStationId id1, final TrainStationId id2) {
        ensureStationIdExist(id1);
        ensureStationIdExist(id2);

        final ImmutableSet<RailwayTrack> newConnections = connections
            .newWith(new RailwayTrack(id1, id2));

        ensureInvariants(stations, newConnections);

        return new RailNetworkDraft(this.id, this.stations, newConnections, this.stationId);
    }

    public RailNetworkDraft withoutConnection(final TrainStationName name1, final TrainStationName name2) {
        return withoutConnection(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withoutConnection(final TrainStationId id1, final TrainStationId id2) {
        final RailwayTrack connection = new RailwayTrack(id1, id2);

        return new RailNetworkDraft(
            this.id,
            this.stations,
            this.connections.reject(track -> track.equals(connection)),
            this.stationId
        );
    }

    public Optional<RailNetworkDraftId> getId() {
        return id;
    }

    public ImmutableSet<TrainStation> getStations() {
        return stations;
    }

    public ImmutableSet<RailwayTrack> getConnections() {
        return connections;
    }

    private TrainStationId getStationIdOf(final TrainStationName name) {
        return stations
            .detect(ts -> ts.getName().equals(name))
            .getId();
    }

    private void ensureInvariants(
        final ImmutableSet<TrainStation> stations,
        final ImmutableSet<RailwayTrack> connections
    ) {
        for (final Invariant invariant : invariants) {
            invariant.ensureIsSatisfied(stations.castToSet(), connections.castToSet());
        }
    }

    private void ensureStationIdExist(final TrainStationId stationId) {
        if (stations.noneSatisfy(ts -> ts.getId().equals(stationId))) {
            throw new IllegalArgumentException(
                "Station ID \"" + stationId + "\" does not exist"
            );
        }
    }
}
