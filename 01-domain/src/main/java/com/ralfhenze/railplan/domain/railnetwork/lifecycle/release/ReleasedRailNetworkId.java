package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.validation.PropertyValidation;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotBlank;

import java.util.List;

public class ReleasedRailNetworkId implements Id {

    private final String id;

    public ReleasedRailNetworkId(final String id) {
        this.id = id;
    }

    @Override
    public boolean isValid() {
        return getValidationErrors().isEmpty();
    }

    public List<ValidationError> getValidationErrors() {
        return new PropertyValidation<>(id)
            .ensureIt(new IsNotBlank())
            .getValidationErrors();
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((ReleasedRailNetworkId)o).id.equals(this.id));
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
