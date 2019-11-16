package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.common.DomainService;

import java.time.LocalDate;
import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Periods of released Rail Network Plans are continuous without gaps and don't overlap
 */
class RailNetworkReleaseService implements DomainService {

    private final RailNetworkRepository railNetworkRepository;

    RailNetworkReleaseService(final RailNetworkRepository railNetworkRepository) {
        this.railNetworkRepository = ensureNotNull(railNetworkRepository, "Rail Network Repository");
    }

    Optional<RailNetwork> release(final RailNetworkDraft draft, final RailNetworkPeriod period) {
        ensureValidPeriod(period);

        RailNetwork railNetwork = new RailNetwork(
            new RailNetworkId("1"),
            period,
            draft.getStations(),
            draft.getConnections()
        );

        railNetworkRepository.persist(railNetwork);

        // TODO: emit RailNetworkReleased event

        return Optional.of(railNetwork);
    }

    private void ensureValidPeriod(final RailNetworkPeriod period) {
        final Optional<RailNetwork> lastReleasedRailNetwork = railNetworkRepository.getLastReleasedRailNetwork();

        if (lastReleasedRailNetwork.isPresent()) {
            LocalDate lastEndDate = lastReleasedRailNetwork.get().getPeriod().getEndDate();
            LocalDate validStartDate = lastEndDate.plusDays(1);
            LocalDate periodStartDate = period.getStartDate();

            // ensure no gaps and no overlapping
            if (!periodStartDate.equals(validStartDate)) {
                throw new IllegalArgumentException(
                    "Period start date should be " + validStartDate + ", but was " + periodStartDate
                );
            }
        }
    }
}
