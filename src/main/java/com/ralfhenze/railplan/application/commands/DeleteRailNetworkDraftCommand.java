package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to delete a whole RailNetworkDraft.
 */
public class DeleteRailNetworkDraftCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the command.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public DeleteRailNetworkDraftCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Deletes given RailNetworkDraft.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public void deleteRailNetworkDraft(final String draftId) {
        draftRepository.deleteRailNetworkDraftOfId(new RailNetworkDraftId(draftId));
    }
}
