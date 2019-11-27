package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.DomainService;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;

import java.time.LocalDate;
import java.util.Optional;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * [x] the Periods of released Rail Network Plans are continuous without gaps and don't overlap
 */
public class RailNetworkReleaseService implements DomainService {

    private final ReleasedRailNetworkRepository railNetworkRepository;

    public RailNetworkReleaseService(final ReleasedRailNetworkRepository railNetworkRepository) {
        this.railNetworkRepository = ensureNotNull(railNetworkRepository, "Rail Network Repository");
    }

    public Optional<ReleasedRailNetwork> release(final RailNetworkDraft draft, final ValidityPeriod period) {
        ensureValidPeriod(period);

        ReleasedRailNetwork railNetwork = new ReleasedRailNetwork(
            period,
            draft.getStations(),
            draft.getTracks()
        );

        return railNetworkRepository.add(railNetwork);
    }

    private void ensureValidPeriod(final ValidityPeriod period) {
        final Optional<ReleasedRailNetwork> lastReleasedRailNetwork = railNetworkRepository.getLastReleasedRailNetwork();

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
