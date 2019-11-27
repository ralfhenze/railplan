package com.ralfhenze.railplan.domain.railnetwork.elements;

import com.ralfhenze.railplan.domain.common.Id;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotBlank;

/**
 * Local Id
 */
public class TrainStationId implements Id {

    private final String id;

    public TrainStationId(final String id) {
        this.id = ensureNotBlank(id, "ID");
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && ((TrainStationId)o).id.equals(this.id));
    }

    @Override
    public String toString() {
        return id;
    }
}