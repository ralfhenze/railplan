package com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft;

import com.ralfhenze.railplan.domain.common.Id;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotBlank;

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
