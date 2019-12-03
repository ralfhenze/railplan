package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.LocalEntity;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotNull;

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
    ) throws ValidationException {
        new Validation()
            .ensureThat(id, new IsNotNull<>(), "Station ID")
            .ensureThat(name, new IsNotNull<>(), "Station Name")
            .ensureThat(location, new IsNotNull<>(), "Geo Location")
            .throwExceptionIfInvalid();

        this.id = id;
        this.name = name;
        this.location = location;
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

    public TrainStation withName(final TrainStationName name) throws ValidationException {
        return new TrainStation(this.id, name, this.location);
    }

    public TrainStation withLocation(
        final GeoLocationInGermany location
    ) throws ValidationException {
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
