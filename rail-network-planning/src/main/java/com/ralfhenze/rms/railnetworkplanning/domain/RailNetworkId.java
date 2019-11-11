package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * TODO: make sure id != null
 */
class RailNetworkId {

    private final String id;

    RailNetworkId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
