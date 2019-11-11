package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;

class RailNetworkId implements Id {

    private final String id;

    RailNetworkId(final String id) {
        this.id = ensureNotBlank(id, "Id must not be blank");
    }

    @Override
    public String toString() {
        return id;
    }
}
