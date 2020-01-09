package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to create and delete Drafts.
 */
public class RailNetworkDraftService implements ApplicationService {

    final private RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the Draft service.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public RailNetworkDraftService(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Creates a new empty Draft.
     */
    public Optional<RailNetworkDraft> addDraft() {
        return draftRepository.persist(new RailNetworkDraft());
    }

    /**
     * Deletes an existing Draft.
     *
     * @throws ValidationException if draftId is not valid
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public void deleteDraft(final DeleteRailNetworkDraftCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        draftRepository.deleteRailNetworkDraftOfId(draftId);
    }
}
