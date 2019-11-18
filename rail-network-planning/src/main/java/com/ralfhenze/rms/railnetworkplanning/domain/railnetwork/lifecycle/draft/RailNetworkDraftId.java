package com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;

public class RailNetworkDraftId implements Id {

    private final String id;

    public RailNetworkDraftId(final String id) {
        this.id = ensureNotBlank(id, "ID");
    }

    @Override
    public String toString() {
        return id;
    }
}
