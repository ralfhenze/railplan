package com.ralfhenze.rms.railnetworkplanning.domain;

import java.time.LocalDate;
import java.util.Optional;

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
class RailNetworkProposal {
    RailNetworkDraftId id;
    SetWithAtLeastTwoElements<TrainStation> stations;
    NonEmptySet<DoubleTrackRailway> connections;

    // or dedicated Release Service
    // (-) wouldn't work at the beginning, when there is nothing released yet
    Optional<ReleasedRailNetwork> releaseAfter(ReleasedRailNetwork releasedNetwork, LocalDate untilDate) {
        return Optional.empty();
    }

    RailNetworkDraft makeModifiable() {
        return new RailNetworkDraft();
    }
}
