package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.DomainService;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Periods of released Rail Network Plans are continuous without gaps and don't overlap
 *     -> release()
 */
class RailNetworkReleaseService implements DomainService {

    private final RailNetworkRepository railNetworkRepository;

    RailNetworkReleaseService(final RailNetworkRepository railNetworkRepository) {
        this.railNetworkRepository = ensureNotNull(railNetworkRepository, "Rail Network Repository");
    }

    Optional<RailNetwork> release(RailNetworkDraft draft, RailNetworkPeriod period) {
        // 1. Make sure period is valid
        // 2. Copy Draft graph into new RailNetwork
        // 3. Make sure RailNetwork invariants are met
        // 4. Persist RailNetwork
        // 5. Emit RailNetworkReleased event

        return Optional.empty();
    }
}
