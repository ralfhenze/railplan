package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.ContainsAtLeastTwoStationsAndOneTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.ContainsNoUnconnectedSubGraphs;

import java.util.Arrays;
import java.util.HashSet;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

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
 * [x] released Rail Network Plans can't be changed any more
 *     -> immutable object, no setters
 */
class RailNetwork implements Aggregate {

    private final RailNetworkId id;
    private final RailNetworkPeriod period;
    private final RailNetworkGraph graph = new RailNetworkGraph(new HashSet<>(Arrays.asList(
        new ContainsAtLeastTwoStationsAndOneTrack(),
        new ContainsNoUnconnectedSubGraphs()
    )));

    RailNetwork(final RailNetworkId id, final RailNetworkPeriod period) {
        this.id = ensureNotNull(id, "Rail Network ID");
        this.period = ensureNotNull(period, "Rail Network Period");
    }
}
