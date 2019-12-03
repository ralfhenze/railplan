package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.Aggregate;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotNull;
import com.ralfhenze.railplan.domain.railnetwork.invariants.*;
import com.ralfhenze.railplan.domain.railnetwork.elements.*;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the minimum distance between two Stations is 10 km
 * [x] a Station's Name is unique
 * [x] two Stations can only be connected by a single Track
 * [x] the maximum length of a Track is 300 km
 */
public class RailNetworkDraft implements Aggregate {

    private final Optional<RailNetworkDraftId> id;
    private final ImmutableList<TrainStation> stations;
    private final ImmutableList<RailwayTrack> tracks;
    private final int stationId;

    public RailNetworkDraft() {
        this(
            Optional.empty(),
            Lists.immutable.empty(),
            Lists.immutable.empty(),
            1
        );
    }

    private RailNetworkDraft(
        final Optional<RailNetworkDraftId> id,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks,
        final int stationId
    ) throws ValidationException {
        new Validation()
            .ensureThat(id, new IsNotNull(), "Rail Network Draft ID")
            .ensureThat(stations, new HasUniqueStationNames(), "Station Name")
            .ensureThat(stations, new HasNoStationsNearerThan10Km(), "Station Name")
            .ensureThat(tracks, new HasNoTracksLongerThan300Km(stations), "Railway Tracks")
            .ensureThat(tracks, new HasNoDuplicateTracks(stations), "Railway Tracks")
            .throwExceptionIfInvalid();

        this.id = id;
        this.stations = stations;
        this.tracks = tracks;
        this.stationId = stationId;
    }

    public RailNetworkDraft withId(RailNetworkDraftId id) {
        ensureNotNull(id, "Rail Network Draft ID");

        return new RailNetworkDraft(Optional.of(id), stations, tracks, stationId);
    }

    public RailNetworkDraft withNewStation(final TrainStationName name, final GeoLocationInGermany location) {
        final var stationId = new TrainStationId(String.valueOf(this.stationId));
        final var addedStation = new TrainStation(stationId, name, location);
        final var newStations = stations.newWith(addedStation);

        return new RailNetworkDraft(this.id, newStations, this.tracks, this.stationId + 1);
    }

    public RailNetworkDraft withRenamedStation(final TrainStationName currentName, final TrainStationName newName) {
        return withRenamedStation(getStationIdOf(currentName), newName);
    }

    public RailNetworkDraft withRenamedStation(final TrainStationId id, final TrainStationName name) {
        ensureStationIdExist(id);

        final var renamedStation = stations
            .detect(ts -> ts.getId().equals(id))
            .withName(name);
        final var newStations = stations
            .reject(ts -> ts.getId().equals(id))
            .newWith(renamedStation);

        return new RailNetworkDraft(this.id, newStations, this.tracks, this.stationId);
    }

    public RailNetworkDraft withMovedStation(final TrainStationName name, final GeoLocationInGermany location) {
        return withMovedStation(getStationIdOf(name), location);
    }

    public RailNetworkDraft withMovedStation(final TrainStationId id, final GeoLocationInGermany location) {
        ensureStationIdExist(id);

        final var movedStation = stations
            .detect(station -> station.getId().equals(id))
            .withLocation(location);
        final var updatedStations = stations
            .reject(station -> station.getId().equals(id))
            .newWith(movedStation);

        return new RailNetworkDraft(this.id, updatedStations, this.tracks, this.stationId);
    }

    public RailNetworkDraft withoutStation(final TrainStationName name) {
        return withoutStation(getStationIdOf(name));
    }

    public RailNetworkDraft withoutStation(final TrainStationId id) {
        return new RailNetworkDraft(
            this.id,
            this.stations.reject(station -> station.getId().equals(id)),
            this.tracks.reject(track -> track.connectsStation(id)),
            this.stationId
        );
    }

    public RailNetworkDraft withNewTrack(final TrainStationName name1, final TrainStationName name2) {
        return withNewTrack(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withNewTrack(final TrainStationId id1, final TrainStationId id2) {
        ensureStationIdExist(id1);
        ensureStationIdExist(id2);

        final var updatedTracks = tracks.newWith(new RailwayTrack(id1, id2));

        return new RailNetworkDraft(this.id, this.stations, updatedTracks, this.stationId);
    }

    public RailNetworkDraft withoutTrack(final TrainStationName name1, final TrainStationName name2) {
        return withoutTrack(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withoutTrack(final TrainStationId id1, final TrainStationId id2) {
        return new RailNetworkDraft(
            this.id,
            this.stations,
            this.tracks.reject(track -> track.equals(new RailwayTrack(id1, id2))),
            this.stationId
        );
    }

    public Optional<RailNetworkDraftId> getId() {
        return id;
    }

    public ImmutableList<TrainStation> getStations() {
        return stations;
    }

    public ImmutableList<RailwayTrack> getTracks() {
        return tracks;
    }

    private TrainStationId getStationIdOf(final TrainStationName name) {
        return stations
            .detect(station -> station.getName().equals(name))
            .getId();
    }

    private void ensureStationIdExist(final TrainStationId stationId) {
        if (stations.noneSatisfy(ts -> ts.getId().equals(stationId))) {
            throw new IllegalArgumentException(
                "Station ID \"" + stationId + "\" does not exist"
            );
        }
    }
}
