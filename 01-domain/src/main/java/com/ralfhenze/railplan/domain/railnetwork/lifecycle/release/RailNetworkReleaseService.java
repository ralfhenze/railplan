package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.DomainService;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
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
     *
     * @throws ValidationException if any Network or Validity Period invariants are violated
     */
    public ReleasedRailNetwork release(
        final RailNetworkDraft draft,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final var lastReleasedRailNetwork = networkRepository.getLastReleasedRailNetwork();

        var period = new ValidityPeriod(startDate, endDate);

        if (lastReleasedRailNetwork.isPresent()) {
            final var lastEndDate = lastReleasedRailNetwork.get().getPeriod().getEndDate();
            period = new ValidityPeriod(startDate, endDate, lastEndDate);
        }

        final var network = new ReleasedRailNetwork(
            period,
            draft.getStations(),
            draft.getTracks()
        );

        return networkRepository.add(network);
    }
}
