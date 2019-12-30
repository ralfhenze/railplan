package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.Validatable;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotBlank;

import java.util.List;

public class TrainStationId implements Id, Validatable {

    private final String id;

    public TrainStationId(final String id) {
        this.id = id;
    }

    @Override
    public boolean isValid() {
        return getValidationErrors().isEmpty();
    }

    public List<ValidationError> getValidationErrors() {
        return new Validation<>(id)
            .ensureIt(new IsNotBlank())
            .getValidationErrors();
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((TrainStationId)o).id.equals(this.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
