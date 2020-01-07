package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class RailNetworkDraftService implements ApplicationService {

    final private RailNetworkDraftRepository draftRepository;

    public RailNetworkDraftService(
        final RailNetworkDraftRepository draftRepository
    ) {
        this.draftRepository = draftRepository;
    }

    public Optional<RailNetworkDraft> addDraft() {
        final var draft = new RailNetworkDraft();

        Optional<RailNetworkDraft> persistedDraft = draftRepository.persist(draft);

        return persistedDraft;
    }

    /**
     * Executes DeleteRailNetworkDraftCommand.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public void deleteDraft(final DeleteRailNetworkDraftCommand command) {
        draftRepository.deleteRailNetworkDraftOfId(
            new RailNetworkDraftId(command.getDraftId())
        );
    }
}
