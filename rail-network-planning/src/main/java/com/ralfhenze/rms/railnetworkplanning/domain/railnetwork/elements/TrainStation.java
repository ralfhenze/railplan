package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements;

import com.ralfhenze.rms.railnetworkplanning.domain.common.LocalEntity;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
public class TrainStation implements LocalEntity {

    private final TrainStationId id;
    private final StationName name;
    private final GeoLocationInGermany location;

    public TrainStation(final TrainStationId id, final StationName name, final GeoLocationInGermany location) {
        this.id = ensureNotNull(id, "Station ID");
        this.name = ensureNotNull(name, "Station Name");
        this.location = ensureNotNull(location, "Geo Location");
    }

    public TrainStationId getId() {
        return id;
    }

    public StationName getName() {
        return name;
    }

    public GeoLocationInGermany getLocation() {
        return location;
    }

    public TrainStation withName(StationName name) {
        return new TrainStation(this.id, name, this.location);
    }

    public TrainStation withLocation(GeoLocationInGermany location) {
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
