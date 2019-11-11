package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.LocalEntity;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
class TrainStation implements LocalEntity {

    private final StationId id;
    private final StationName name;
    private final GeoLocationInGermany location;

    TrainStation(final StationId id, final StationName name, final GeoLocationInGermany location) {
        this.id = ensureNotNull(id, "Id is required");
        this.name = ensureNotNull(name, "Name is required");
        this.location = ensureNotNull(location, "Location is required");
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
}
