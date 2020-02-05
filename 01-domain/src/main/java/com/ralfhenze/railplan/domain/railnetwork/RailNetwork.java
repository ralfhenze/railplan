package com.ralfhenze.railplan.domain.railnetwork;

import com.ralfhenze.railplan.domain.common.Aggregate;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoDuplicateTracks;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoTracksLongerThan300Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasUniqueStationNames;
import com.ralfhenze.railplan.domain.railnetwork.invariants.IsNotNearerThan10KmTo;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
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
public class RailNetwork implements Aggregate {

    private final Optional<RailNetworkId> id;
    private final ImmutableList<TrainStation> stations;
    private final ImmutableList<RailwayTrack> tracks;

    /**
     * Constructs an empty Network without Stations or Tracks.
     */
    public RailNetwork() {
        this(
            Optional.empty(),
            Lists.immutable.empty(),
            Lists.immutable.empty()
        );
    }

    /**
     * @throws ValidationException if any Network invariants are violated
     */
    private RailNetwork(
        final Optional<RailNetworkId> id,
        final ImmutableList<TrainStation> stations,
        final ImmutableList<RailwayTrack> tracks
    ) {
        new Validation()
            .ensureThat(stations, new HasUniqueStationNames(), Field.STATION_NAME)
            .ensureThat(tracks, new HasNoTracksLongerThan300Km(stations), Field.TRACKS)
            .ensureThat(tracks, new HasNoDuplicateTracks(stations), Field.TRACKS)
            .throwExceptionIfInvalid();

        this.id = id;
        this.stations = stations;
        this.tracks = tracks;
    }

    /**
     * Returns a new Network with given Network ID.
     *
     * @throws IllegalArgumentException if given ID is null
     */
    public RailNetwork withId(RailNetworkId id) {
        ensureNotNull(id, "Rail Network ID");

        return new RailNetwork(Optional.of(id), stations, tracks);
    }

    /**
     * Returns a new Network with added preset Stations.
     *
     * @throws ValidationException if Station Name, Location or Network invariants are violated
     */
    public RailNetwork addStations(final PresetStation... presetStations) {
        var network = this;
        for (final var presetStation : presetStations) {
            network = network.addStation(
                presetStation.getName(),
                presetStation.getLatitude(),
                presetStation.getLongitude()
            );
        }

        return network;
    }

    /**
     * Returns a new Network with added Station. The new Station will be appended to the
     * end of the Stations list.
     *
     * @throws ValidationException if Station Name, Location or Network invariants are violated
     */
    public RailNetwork addStation(
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        return addStation(getNextStationId(), stationName, latitude, longitude);
    }

    /**
     * Returns a new Network with added Station. The new Station will be appended to the
     * end of the Stations list.
     *
     * @throws ValidationException if Station Name, Location or Network invariants are violated
     *                             or Station ID already exists
     */
    public RailNetwork addStation(
        final int stationId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        ensureStationIdDoesNotExistAlready(stationId);

        final var v = new Validation();
        final var id = v.get(() -> new TrainStationId(stationId));
        final var name = v.get(() -> new TrainStationName(stationName, getOtherStationNames()));
        final var location = v.get(() -> new GeoLocationInGermany(latitude, longitude));
        validateLocationDistance(location, v, stations);
        v.throwExceptionIfInvalid();

        final var addedStation = new TrainStation(id, name, location);
        final var newStations = stations.newWith(addedStation);

        return new RailNetwork(this.id, newStations, this.tracks);
    }

    /**
     * Returns a new Network with updated Station name and location.
     *
     * @throws EntityNotFoundException if TrainStation with stationId does not exist
     * @throws ValidationException if Station Name, Location or Network invariants are violated
     */
    public RailNetwork updateStation(
        final int stationId,
        final String newStationName,
        final double newLatitude,
        final double newLongitude
    ) {
        final var otherStations = getOtherStationsWithout(stationId);
        final var otherStationNames = getOtherStationNamesWithout(stationId);

        final var v = new Validation();
        final var id = v.get(() -> new TrainStationId(stationId));
        final var newName = v.get(() -> new TrainStationName(newStationName, otherStationNames));
        final var newLocation = v.get(() -> new GeoLocationInGermany(newLatitude, newLongitude));
        validateLocationDistance(newLocation, v, otherStations);
        v.throwExceptionIfInvalid();

        ensureStationExists(id);

        final var stationIndex = stations.detectIndex(s -> s.getId().equals(id));
        final var mutableStations = Lists.mutable.ofAll(stations);
        mutableStations.set(stationIndex, stations.get(stationIndex)
            .withName(newName)
            .withLocation(newLocation)
        );
        final var newStations = mutableStations.toImmutable();

        return new RailNetwork(this.id, newStations, this.tracks);
    }

    /**
     * Returns a new Network without the Station of given name. The Tracks connected to this
     * Station will also be removed.
     *
     * @throws EntityNotFoundException if TrainStation with stationName does not exist
     */
    public RailNetwork deleteStation(final String stationName) {
        return deleteStation(getStationIdOf(stationName).getId());
    }

    /**
     * Returns a new Network without the Station of given ID. The Tracks connected to this
     * Station will also be removed.
     *
     * @throws ValidationException if stationId is invalid
     * @throws EntityNotFoundException if TrainStation with stationId does not exist
     */
    public RailNetwork deleteStation(final int stationId) {
        final var id = new TrainStationId(stationId);

        ensureStationExists(id);

        return new RailNetwork(
            this.id,
            this.stations.reject(station -> station.getId().equals(id)),
            this.tracks.reject(track -> track.connectsStation(id))
        );
    }

    /**
     * Returns a new Network with added Track appended to the end of the Tracks list.
     *
     * @throws ValidationException if any Track or Network invariants are violated
     */
    public RailNetwork addTrackBetween(
        final PresetStation firstStation,
        final PresetStation secondStation
    ) {
        return addTrackBetween(firstStation.getName(), secondStation.getName());
    }

    /**
     * Returns a new Network with added Track appended to the end of the Tracks list.
     *
     * @throws ValidationException if any Track or Network invariants are violated
     */
    public RailNetwork addTrackBetween(
        final String firstStationName,
        final String secondStationName
    ) {
        return addTrackBetween(
            getStationIdOf(firstStationName).getId(),
            getStationIdOf(secondStationName).getId()
        );
    }

    /**
     * Returns a new Network with added Track appended to the end of the Tracks list.
     *
     * @throws ValidationException if any Station ID, Track or Network invariants are violated
     */
    public RailNetwork addTrackBetween(final int firstStationId, final int secondStationId) {
        final var v = new Validation();
        final var firstId = v.get(() -> new TrainStationId(firstStationId));
        final var secondId = v.get(() -> new TrainStationId(secondStationId));
        v.throwExceptionIfInvalid();

        ensureStationExists(firstId);
        ensureStationExists(secondId);

        final var updatedTracks = tracks.newWith(new RailwayTrack(firstId, secondId));

        return new RailNetwork(this.id, this.stations, updatedTracks);
    }

    /**
     * Returns a new Network without the Track between the Stations of given names.
     *
     * @throws EntityNotFoundException if TrainStation of stationName or Track does not exist
     */
    public RailNetwork deleteTrackBetween(
        final String firstStationName,
        final String secondStationName
    ) {
        return deleteTrackBetween(
            getStationIdOf(firstStationName).getId(),
            getStationIdOf(secondStationName).getId()
        );
    }

    /**
     * Returns a new Network without the Track between the Stations of given IDs.
     *
     * @throws ValidationException if any stationId is invalid
     * @throws EntityNotFoundException if TrainStation of stationId or Track does not exist
     */
    public RailNetwork deleteTrackBetween(
        final int firstStationId,
        final int secondStationId
    ) {
        final var v = new Validation();
        final var firstId = v.get(() -> new TrainStationId(firstStationId));
        final var secondId = v.get(() -> new TrainStationId(secondStationId));
        v.throwExceptionIfInvalid();

        ensureStationExists(firstId);
        ensureStationExists(secondId);
        ensureTrackExists(firstId, secondId);

        final var trackToBeDeleted = new RailwayTrack(firstId, secondId);

        return new RailNetwork(
            this.id,
            this.stations,
            this.tracks.reject(track -> track.equals(trackToBeDeleted))
        );
    }

    public Optional<RailNetworkId> getId() {
        return id;
    }

    public ImmutableList<TrainStation> getStations() {
        return stations;
    }

    public ImmutableList<RailwayTrack> getTracks() {
        return tracks;
    }

    private TrainStationId getStationIdOf(final String name) {
        return stations
            .detectOptional(station -> station.getName().getName().equals(name))
            .orElseThrow(() -> new EntityNotFoundException("Station", "name", name))
            .getId();
    }

    private ImmutableList<String> getOtherStationNames() {
        return stations.collect(s -> s.getName().getName());
    }

    private ImmutableList<String> getOtherStationNamesWithout(final int stationId) {
        return getOtherStationsWithout(stationId)
            .collect(s -> s.getName().getName());
    }

    private ImmutableList<TrainStation> getOtherStationsWithout(final int stationId) {
        return stations.reject(s -> s.getId().getId() == stationId);
    }

    private void validateLocationDistance(
        final GeoLocationInGermany location,
        final Validation validation,
        final ImmutableList<TrainStation> stations
    ) {
        if (location != null) {
            validation.ensureThat(location, new IsNotNearerThan10KmTo(stations), Field.LOCATION);
        }
    }

    private void ensureTrackExists(
        final TrainStationId stationId1,
        final TrainStationId stationId2
    ) {
        final var track = new RailwayTrack(stationId1, stationId2);
        if (tracks.noneSatisfy(t -> t.equals(track))) {
            throw new EntityNotFoundException(
                "Couldn't find Track of Station ID \""
                    + stationId1.toString() + "\" and \""
                    + stationId2.toString() + "\""
            );
        }
    }

    private void ensureStationExists(final TrainStationId stationId) {
        if (stations.noneSatisfy(s -> s.getId().equals(stationId))) {
            throw new EntityNotFoundException("Station", stationId.toString());
        }
    }

    private void ensureStationIdDoesNotExistAlready(final int stationId) {
        if (stations.anySatisfy(s -> s.getId().getId() == stationId)) {
            throw new ValidationException(Lists.immutable.of(
                new ValidationError(stationId + " already exists", Field.STATION_ID)
            ));
        }
    }

    private int getNextStationId() {
        if (stations.isEmpty()) {
            return 1;
        } else {
            return stations.collect(s -> s.getId().getId()).max() + 1;
        }
    }
}
