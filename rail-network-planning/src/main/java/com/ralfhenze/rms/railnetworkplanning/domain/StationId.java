package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

/**
 * Local Id
 *
 * TODO: make sure id != null
 */

class StationId implements Id {

    private final String id;

    StationId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
