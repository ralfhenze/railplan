package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.LocalEntity;
import com.ralfhenze.railplan.domain.common.Validatable;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;

import java.util.List;

/**
 * https://en.wikipedia.org/wiki/Train_station
 *
 * Local Entity within RailNetwork Aggregate
 */
public class TrainStation implements LocalEntity, Validatable {

    private final TrainStationId id;
    private final TrainStationName name;
    private final GeoLocationInGermany location;

    public TrainStation(
        final TrainStationId id,
        final TrainStationName name,
        final GeoLocationInGermany location
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public boolean isValid() {
        return id.isValid() && name.isValid() && location.isValid();
    }

    public List<ValidationError> getValidationErrors() {
        return List.of();
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
