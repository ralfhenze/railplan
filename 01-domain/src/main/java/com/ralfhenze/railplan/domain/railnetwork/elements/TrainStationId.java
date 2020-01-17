package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsPositive;

public class TrainStationId implements Id {

    private final int id;

    /**
     * @throws ValidationException if id is negative or zero
     */
    public TrainStationId(final int id) {
        new Validation()
            .ensureThat(id, new IsPositive(), Field.STATION_ID)
            .throwExceptionIfInvalid();

        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((TrainStationId)o).id == this.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
