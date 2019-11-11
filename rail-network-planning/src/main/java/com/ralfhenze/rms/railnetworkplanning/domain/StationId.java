package com.ralfhenze.rms.railnetworkplanning.domain;

/**
 * Local Id
 *
 * TODO: make sure id != null
 */

class StationId {

    private final String id;

    StationId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
