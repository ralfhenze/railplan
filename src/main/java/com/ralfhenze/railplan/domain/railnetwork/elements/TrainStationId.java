package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotBlank;

public class TrainStationId implements Id {

    private final String id;

    public TrainStationId(final String id) throws ValidationException {
        new Validation()
            .ensureThat(id, new IsNotBlank(), "Train Station ID")
            .throwExceptionIfInvalid();

        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((TrainStationId)o).id.equals(this.id));
    }

    @Override
    public String toString() {
        return id;
    }
}
