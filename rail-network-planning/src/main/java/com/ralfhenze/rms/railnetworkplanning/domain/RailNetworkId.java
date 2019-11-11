package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotBlank;

class RailNetworkId implements Id {

    private final String id;

    RailNetworkId(final String id) {
        assertNotBlank(id, "Id must not be blank");
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
