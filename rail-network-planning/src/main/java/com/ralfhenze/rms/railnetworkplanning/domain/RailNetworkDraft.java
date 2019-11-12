package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.station.*;

import java.util.*;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the minimum distance between two Stations is 10 km
 * [x] a Station's Name is unique
 * [x] two Stations can only be connected by a single Track
 * [x] the maximum length of a Track is 200 km
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
        ensureMinimumStationDistance(location);

        final StationId stationId = new StationId(String.valueOf(stations.size() + 1));
        stations.put(stationId, new TrainStation(stationId, name, location));

        return stationId;
    }

    public void renameStation(final StationId id, final StationName name) {
        ensureStationIdExist(id);
        ensureStationNameDoesNotExist(name);

        stations.put(id, stations.get(id).withName(name));
    }

    public void moveStation(final StationId id, final GeoLocationInGermany location) {
        ensureStationIdExist(id);
        ensureMinimumStationDistance(location, id);

        stations.put(id, stations.get(id).withLocation(location));
    }

    public void deleteStation(final StationId id) {
    }

    public void connectStations(final StationId id1, final StationId id2) {
        ensureStationIdExist(id1);
        ensureStationIdExist(id2);
        ensureConnectionDoesNotExist(id1, id2);
        ensureMaximumTrackLength(id1, id2);

        connections.add(new DoubleTrackRailway(id1, id2));
    }

    public void disconnectStations(final StationId id1, final StationId id2) {
    }

    public RailNetworkGraph getGraph() {
        return graph;
    }

    private void ensureStationNameDoesNotExist(final StationName stationName) {
        final boolean nameExists = stations.values().stream()
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

    private void ensureMinimumStationDistance(final GeoLocationInGermany location) {
        ensureMinimumStationDistance(location, null);
    }

    private void ensureMinimumStationDistance(final GeoLocationInGermany location, final StationId ignoredId) {
        final Optional<TrainStation> nearbyStation = stations
            .values()
            .stream()
            .filter(station -> !station.getId().equals(ignoredId))
            .filter(station ->
                station
                    .getLocation()
                    .getLocation()
                    .getKilometerDistanceTo(location.getLocation()) <= 10.0)
            .findFirst();

        if (nearbyStation.isPresent()) {
            double distance = nearbyStation.get().getLocation()
                .getLocation().getKilometerDistanceTo(location.getLocation());

            throw new IllegalArgumentException(
                "Distance to Station \""
                    + nearbyStation.get().getName()
                    + "\" should be > 10 km, but was "
                    + (Math.round(distance * 100.0) / 100.0)
                    + " km"
            );
        }
    }

    private void ensureConnectionDoesNotExist(final StationId id1, final StationId id2) {
        final DoubleTrackRailway newConnection = new DoubleTrackRailway(id1, id2);
        final boolean connectionExists = connections.stream()
            .anyMatch(connection -> connection.equals(newConnection));

        if (connectionExists) {
            throw new IllegalArgumentException(
                "Connection from \""
                    + stations.get(id1).getName() + "\" (" + id1 + ") to \""
                    + stations.get(id2).getName() + "\" (" + id2 + ") already exists"
            );
        }
    }

    private void ensureMaximumTrackLength(final StationId id1, final StationId id2) {
        final double trackLength = stations.get(id1)
            .getLocation()
            .getLocation()
            .getKilometerDistanceTo(
                stations.get(id2).getLocation().getLocation()
            );

        if (trackLength > 200) {
            throw new IllegalArgumentException(
                "Track from \""
                    + stations.get(id1).getName() + "\" (" + id1 + ") to \""
                    + stations.get(id2).getName() + "\" (" + id2 + ") must be shorter than 200 km"
                    + ", but was ~" + Math.round(trackLength) + " km"
            );
        }
    }
}
