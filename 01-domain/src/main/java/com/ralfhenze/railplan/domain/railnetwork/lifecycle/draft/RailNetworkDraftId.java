package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.Id;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.common.validation.constraints.IsNotBlank;

public class RailNetworkDraftId implements Id {

    private final String id;

    public RailNetworkDraftId(final String id) throws ValidationException {
        new Validation()
            .ensureThat(id, new IsNotBlank(), "Rail Network Draft ID")
            .throwExceptionIfInvalid();

        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((RailNetworkDraftId)o).id.equals(this.id));
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
