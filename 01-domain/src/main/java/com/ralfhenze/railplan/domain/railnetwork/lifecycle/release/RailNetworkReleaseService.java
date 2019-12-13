package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.DomainService;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;

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
     * @throws ValidationException if Period is invalid or Draft violates Network invariants
     */
    public ReleasedRailNetwork release(final RailNetworkDraft draft, final ValidityPeriod period) {
        validatePeriod(period);

        final var network = new ReleasedRailNetwork(
            period,
            draft.getStations(),
            draft.getTracks()
        );

        return networkRepository.add(network);
    }

    private void validatePeriod(final ValidityPeriod period) {
        final var lastReleasedRailNetwork = networkRepository.getLastReleasedRailNetwork();

        if (lastReleasedRailNetwork.isPresent()) {
            final var lastEndDate = lastReleasedRailNetwork.get().getPeriod().getEndDate();
            final var validStartDate = lastEndDate.plusDays(1);
            final var periodStartDate = period.getStartDate();

            // ensure no gaps and no overlapping
            if (!periodStartDate.equals(validStartDate)) {
                throw new ValidationException(
                    Maps.immutable.of(
                        "Start Date",
                        Lists.mutable.of(
                            "Start Date should be " + validStartDate
                                + ", but was " + periodStartDate
                        )
                    )
                );
            }
        }
    }
}
