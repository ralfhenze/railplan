package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;

import java.util.HashSet;
import java.util.Set;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotNull;

/**
 * READ-ONLY
 *
 * [x] released Rail Network Plans can't be changed any more
 *     -> immutable object, no setters
 */
class ReleasedRailNetwork implements Aggregate {

    private final RailNetworkId id;
    private final RailNetworkPeriod period;
    private final Set<TrainStation> stations = new HashSet<>(); // Non-empty with at least two elements
    private final Set<DoubleTrackRailway> connections = new HashSet<>(); // Non-empty

    ReleasedRailNetwork(final RailNetworkId id, final RailNetworkPeriod period) {
        assertNotNull(id, "Id is required");
        assertNotNull(period, "Period is required");

        this.id = id;
        this.period = period;
    }
}
