package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.Aggregate;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.ContainsAtLeastTwoStationsAndOneTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.invariants.ContainsNoUnconnectedSubGraphs;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Rail Network Plan contains at least two Stations and one Track
 * [x] the Rail Network Plan is a single graph without unconnected sub-graphs
 * [ ] released Rail Network Plans can't be changed any more
 * [ ] all invariants of RailNetworkDraft
 */
class RailNetwork implements Aggregate {

    private final RailNetworkId id;
    private final RailNetworkPeriod period;
    private final RailNetworkGraph graph;

    RailNetwork(
        final RailNetworkId id,
        final RailNetworkPeriod period,
        final RailNetworkGraph graph
    ) {
        this.id = ensureNotNull(id, "Rail Network ID");
        this.period = ensureNotNull(period, "Rail Network Period");
        this.graph = ensureNotNull(graph, "Rail Network Graph")
            .withAdditionalInvariant(new ContainsAtLeastTwoStationsAndOneTrack())
            .withAdditionalInvariant(new ContainsNoUnconnectedSubGraphs());

        this.graph.ensureInvariants();
    }
}
