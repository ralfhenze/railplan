package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;

public class RailNetworkId implements Id {

    private final String id;

    RailNetworkId(final String id) {
        this.id = ensureNotBlank(id, "ID");
    }

    @Override
    public String toString() {
        return id;
    }
}
