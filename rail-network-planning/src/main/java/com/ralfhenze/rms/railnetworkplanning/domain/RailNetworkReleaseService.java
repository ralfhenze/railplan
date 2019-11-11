package com.ralfhenze.rms.railnetworkplanning.domain;

import java.util.Optional;

/**
 * [x] the Periods of released Rail Network Plans are continuous without gaps and don't overlap
 *     -> release()
 *
 * Needs to publish RailNetworkReleased event
 */
class RailNetworkReleaseService {
    ReleasedRailNetworkRepository releasedRailNetworkRepository;

    Optional<ReleasedRailNetwork> release(RailNetworkProposal proposal, RailNetworkPeriod period) {
        return Optional.empty();
    }
}
