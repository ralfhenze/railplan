package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.assertNotNull;

/**
 * READ-ONLY
 *
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan contains no stand-alone / unconnected Stations
 * [x] the Rail Network Plan is a single graph without unconnected islands / sub-graphs
 *     -> Smart Constructor with RailNetworkDraft as input
 *        (otherwise I would need to ensure the same invariants again)
 *        (-) would require a "new ValidatedRailNetwork(this)" in RailNetworkDraft.validate()
 *        (-) circular dependency
 *     -> or dedicated Validation Service
 */
class RailNetworkProposal implements Aggregate {

    private final RailNetworkDraftId id;
    private final Set<TrainStation> stations = new HashSet<>(); // Non-Empty with at least two elements
    private final Set<DoubleTrackRailway> connections = new HashSet<>(); // Non-Empty

    RailNetworkProposal(final RailNetworkDraftId id) {
        assertNotNull(id, "Id is required");
        this.id = id;
    }

    // or dedicated Release Service
    // (-) wouldn't work at the beginning, when there is nothing released yet
    Optional<ReleasedRailNetwork> releaseAfter(ReleasedRailNetwork releasedNetwork, LocalDate untilDate) {
        return Optional.empty();
    }

    RailNetworkDraft makeModifiable() {
        return new RailNetworkDraft(new RailNetworkDraftId("1"));
    }
}
