package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

/**
 * TODO: make sure id != null
 */
class RailNetworkDraftId implements Id {

    private final String id;

    RailNetworkDraftId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
