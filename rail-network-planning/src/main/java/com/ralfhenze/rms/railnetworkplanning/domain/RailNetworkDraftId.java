package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * TODO: make sure id != null
 */
class RailNetworkDraftId {

    private final String id;

    RailNetworkDraftId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
