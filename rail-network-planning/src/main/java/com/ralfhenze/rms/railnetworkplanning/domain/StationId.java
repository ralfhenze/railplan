package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;

/**
 * Local Id
 */
class StationId implements Id {

    private final String id;

    StationId(final String id) {
        this.id = ensureNotBlank(id, "Id must not be blank");
    }

    @Override
    public String toString() {
        return id;
    }
}
