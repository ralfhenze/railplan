package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotBlank;

/**
 * Local Id
 */
class StationId implements Id {

    private final String id;

    StationId(final String id) {
        assertNotBlank(id, "Id must not be blank");
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
