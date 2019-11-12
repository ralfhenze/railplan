package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.LocalEntity;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
public class TrainStation implements LocalEntity {

    private final StationId id;
    private final StationName name;
    private final GeoLocationInGermany location;

    public TrainStation(final StationId id, final StationName name, final GeoLocationInGermany location) {
        this.id = ensureNotNull(id, "Station ID");
        this.name = ensureNotNull(name, "Station Name");
        this.location = ensureNotNull(location, "Geo Location");
    }

    public StationId getId() {
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
}
