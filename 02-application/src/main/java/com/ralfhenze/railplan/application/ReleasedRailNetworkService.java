package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.RailNetworkReleaseService;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to release a RailNetworkDraft.
 */
public class ReleasedRailNetworkService implements ApplicationService {

    final private RailNetworkDraftRepository draftRepository;
    final private RailNetworkReleaseService releaseService;

    /**
     * Constructs the Network service.
     *
     * @throws IllegalArgumentException if draftRepository or networkRepository is null
     */
    public ReleasedRailNetworkService(
        final RailNetworkDraftRepository draftRepository,
        final ReleasedRailNetworkRepository networkRepository
    ) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
        this.releaseService = new RailNetworkReleaseService(networkRepository);
    }

    /**
     * Releases an existing Draft.
     *
     * @throws ValidationException if draftId is not valid or Network invariants are violated
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public ReleasedRailNetwork releaseDraft(final ReleaseRailNetworkCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var releasedRailNetwork = releaseService
            .release(draft, command.getStartDate(), command.getEndDate());

        return releasedRailNetwork;
    }
}
