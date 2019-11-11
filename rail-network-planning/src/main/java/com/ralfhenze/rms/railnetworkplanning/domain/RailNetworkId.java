package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

/**
 * TODO: make sure id != null
 */
class RailNetworkId implements Id {

    private final String id;

    RailNetworkId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
