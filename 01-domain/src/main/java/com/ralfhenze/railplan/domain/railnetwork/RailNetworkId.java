package com.ralfhenze.railplan.domain.railnetwork;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.validation.Field;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotBlank;

public class RailNetworkId implements Id {

    private final String id;

    /**
     * @throws ValidationException if id is blank
     */
    public RailNetworkId(final String id) {
        new Validation()
            .ensureThat(id, new IsNotBlank(), Field.NETWORK_ID)
            .throwExceptionIfInvalid();

        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((RailNetworkId)o).id.equals(this.id));
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
