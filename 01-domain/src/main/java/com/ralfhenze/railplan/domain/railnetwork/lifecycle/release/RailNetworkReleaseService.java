package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.DomainService;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;

import java.time.LocalDate;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Periods of released Rail Network Plans are continuous without gaps and don't overlap
 */
public class RailNetworkReleaseService implements DomainService {

    private final ReleasedRailNetworkRepository networkRepository;

    public RailNetworkReleaseService(final ReleasedRailNetworkRepository networkRepository) {
        this.networkRepository = ensureNotNull(
            networkRepository, "Rail Network Repository"
        );
    }

    /**
     * Releases given Draft for given Period.
     */
    public ReleasedRailNetwork release(
        final RailNetworkDraft draft,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final var lastReleasedRailNetwork = networkRepository.getLastReleasedRailNetwork();

        LocalDate lastEndDate = null;
        if (lastReleasedRailNetwork.isPresent()) {
            lastEndDate = lastReleasedRailNetwork.get().getPeriod().getEndDate();
        }

        final var network = new ReleasedRailNetwork(
            new ValidityPeriod(startDate, endDate, lastEndDate),
            draft.getStations(),
            draft.getTracks()
        );

        if (!network.isValid()) {
            return network;
        }

        return networkRepository.add(network);
    }
}
