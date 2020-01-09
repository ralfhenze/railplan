package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.LocalEntity;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
public class TrainStation implements LocalEntity {

    private final TrainStationId id;
    private final TrainStationName name;
    private final GeoLocationInGermany location;

    public TrainStation(
        final TrainStationId id,
        final TrainStationName name,
        final GeoLocationInGermany location
    ) {
        this.id = ensureNotNull(id, "TrainStationId");
        this.name = ensureNotNull(name, "TrainStationName");
        this.location = ensureNotNull(location, "GeoLocationInGermany");
    }

    public TrainStationId getId() {
        return id;
    }

    public TrainStationName getName() {
        return name;
    }

    public GeoLocationInGermany getLocation() {
        return location;
    }

    public TrainStation withName(final TrainStationName name) {
        return new TrainStation(this.id, name, this.location);
    }

    public TrainStation withLocation(final GeoLocationInGermany location) {
        return new TrainStation(this.id, this.name, location);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((TrainStation)o).id.equals(this.id));
    }

    @Override
    public String toString() {
        return name.getName();
    }
}
