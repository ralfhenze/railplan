package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.Aggregate;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoDuplicateTracks;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoStationsNearerThan10Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasNoTracksLongerThan300Km;
import com.ralfhenze.railplan.domain.railnetwork.invariants.HasUniqueStationNames;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.List;
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
    ) {
        this.id = id;
        this.stations = stations;
        this.tracks = tracks;
        this.stationId = stationId;
    }

    @Override
    public boolean isValid() {
        return id.map(RailNetworkDraftId::isValid).orElse(true)
            && stations.allSatisfy(TrainStation::isValid)
            && tracks.allSatisfy(RailwayTrack::isValid)
            && getStationErrors().isEmpty()
            && getTrackErrors().isEmpty();
    }

    public List<ValidationError> getStationErrors() {
        return new Validation<>(stations)
            .ensureIt(new HasUniqueStationNames())
            .ensureIt(new HasNoStationsNearerThan10Km())
            .getValidationErrors();
    }

    public List<ValidationError> getTrackErrors() {
        return new Validation<>(tracks)
            .ensureIt(new HasNoTracksLongerThan300Km(stations))
            .ensureIt(new HasNoDuplicateTracks(stations))
            .getValidationErrors();
    }

    public RailNetworkDraft withId(RailNetworkDraftId id) {
        ensureNotNull(id, "Rail Network Draft ID");

        return new RailNetworkDraft(Optional.of(id), stations, tracks, stationId);
    }

    public RailNetworkDraft withNewStation(
        final TrainStationName stationName,
        final GeoLocationInGermany location
    ) {
        final var stationId = new TrainStationId(String.valueOf(this.stationId));
        final var addedStation = new TrainStation(stationId, stationName, location);
        final var newStations = stations.newWith(addedStation);

        return new RailNetworkDraft(this.id, newStations, this.tracks, this.stationId + 1);
    }

    /**
     * Returns a new Draft with updated Station name and location.
     *
     * @throws EntityNotFoundException if TrainStation with currentStationName does not exist
     */
    public RailNetworkDraft withUpdatedStation(
        final TrainStationName currentStationName,
        final TrainStationName newStationName,
        final GeoLocationInGermany newLocation
    ) {
        return withUpdatedStation(getStationIdOf(currentStationName), newStationName, newLocation);
    }

    /**
     * Returns a new Draft with updated Station name and location.
     *
     * @throws EntityNotFoundException if TrainStation with stationId does not exist
     */
    public RailNetworkDraft withUpdatedStation(
        final TrainStationId stationId,
        final TrainStationName newStationName,
        final GeoLocationInGermany newLocation
    ) {
        ensureStationExists(stationId);

        final var stationIndex = stations.detectIndex(s -> s.getId().equals(stationId));
        final var mutableStations = Lists.mutable.ofAll(stations);
        mutableStations.set(stationIndex, stations.get(stationIndex)
            .withName(newStationName)
            .withLocation(newLocation)
        );
        final var newStations = mutableStations.toImmutable();

        return new RailNetworkDraft(this.id, newStations, this.tracks, this.stationId);
    }

    /**
     * Returns a new Draft without the Station of given name. The Tracks connected to this
     * Station will also be removed.
     *
     * @throws EntityNotFoundException if TrainStation with stationName does not exist
     */
    public RailNetworkDraft withoutStation(final TrainStationName stationName) {
        return withoutStation(getStationIdOf(stationName));
    }

    /**
     * Returns a new Draft without the Station of given ID. The Tracks connected to this
     * Station will also be removed.
     *
     * @throws EntityNotFoundException if TrainStation with stationId does not exist
     */
    public RailNetworkDraft withoutStation(final TrainStationId stationId) {
        ensureStationExists(stationId);

        return new RailNetworkDraft(
            this.id,
            this.stations.reject(station -> station.getId().equals(stationId)),
            this.tracks.reject(track -> track.connectsStation(stationId)),
            this.stationId
        );
    }

    public RailNetworkDraft withNewTrack(final TrainStationName name1, final TrainStationName name2) {
        return withNewTrack(getStationIdOf(name1), getStationIdOf(name2));
    }

    public RailNetworkDraft withNewTrack(final TrainStationId id1, final TrainStationId id2) {
        ensureStationExists(id1);
        ensureStationExists(id2);

        final var updatedTracks = tracks.newWith(new RailwayTrack(id1, id2));

        return new RailNetworkDraft(this.id, this.stations, updatedTracks, this.stationId);
    }

    /**
     * Returns a new Draft without the Track between the Stations of given names.
     *
     * @throws EntityNotFoundException if TrainStation of stationName or Track does not exist
     */
    public RailNetworkDraft withoutTrack(
        final TrainStationName stationName1,
        final TrainStationName stationName2
    ) {
        return withoutTrack(getStationIdOf(stationName1), getStationIdOf(stationName2));
    }

    /**
     * Returns a new Draft without the Track between the Stations of given IDs.
     *
     * @throws EntityNotFoundException if TrainStation of stationId or Track does not exist
     */
    public RailNetworkDraft withoutTrack(
        final TrainStationId stationId1,
        final TrainStationId stationId2
    ) {
        ensureStationExists(stationId1);
        ensureStationExists(stationId2);
        ensureTrackExists(stationId1, stationId2);

        final var trackToBeDeleted = new RailwayTrack(stationId1, stationId2);

        return new RailNetworkDraft(
            this.id,
            this.stations,
            this.tracks.reject(track -> track.equals(trackToBeDeleted)),
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
            .detectOptional(station -> station.getName().equals(name))
            .orElseThrow(() -> new EntityNotFoundException("Station", "name", name.getName()))
            .getId();
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
}
