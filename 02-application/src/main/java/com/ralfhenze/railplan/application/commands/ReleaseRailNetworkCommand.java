package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.RailNetworkReleaseService;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ValidityPeriod;

import java.time.LocalDate;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to release a RailNetworkDraft.
 */
public class ReleaseRailNetworkCommand implements Command {

    final private RailNetworkDraftRepository draftRepository;
    final private RailNetworkReleaseService releaseService;

    /**
     * Constructs the command.
     *
     * @throws IllegalArgumentException if draftRepository or networkRepository is null
     */
    public ReleaseRailNetworkCommand(
        final RailNetworkDraftRepository draftRepository,
        final ReleasedRailNetworkRepository networkRepository
    ) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
        this.releaseService = new RailNetworkReleaseService(networkRepository);
    }

    /**
     * Releases given Rail Network Draft for period between given dates.
     *
     * @throws EntityNotFoundException if Rail Network Draft with draftId does not exist
     * @throws ValidationException if Draft violates Network invariants or dates are invalid
     */
    public ReleasedRailNetworkId releaseRailNetworkDraft(
        final String draftId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        final var releasedRailNetwork = releaseService
            .release(draft, new ValidityPeriod(startDate, endDate));

        return releasedRailNetwork.getId().get();
    }
}
