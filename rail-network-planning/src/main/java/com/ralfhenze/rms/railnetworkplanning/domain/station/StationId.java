package com.ralfhenze.rms.railnetworkplanning.domain.station;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Id;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotBlank;

/**
 * Local Id
 */
public class StationId implements Id {

    private final String id;

    public StationId(final String id) {
        this.id = ensureNotBlank(id, "ID");
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((StationId)o).id.equals(this.id));
    }

    @Override
    public String toString() {
        return id;
    }
}
