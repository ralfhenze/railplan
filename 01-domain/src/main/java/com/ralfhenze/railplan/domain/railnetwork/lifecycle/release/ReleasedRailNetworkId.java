package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotBlank;

public class ReleasedRailNetworkId implements Id {

    private final String id;

    public ReleasedRailNetworkId(final String id) throws ValidationException {
        new Validation()
            .ensureThat(id, new IsNotBlank(), "Released Rail Network ID")
            .throwExceptionIfInvalid();

        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
