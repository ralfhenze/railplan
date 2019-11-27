package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.Id;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotBlank;

public class ReleasedRailNetworkId implements Id {

    private final String id;

    public ReleasedRailNetworkId(final String id) {
        this.id = ensureNotBlank(id, "ID");
    }

    @Override
    public String toString() {
        return id;
    }
}
