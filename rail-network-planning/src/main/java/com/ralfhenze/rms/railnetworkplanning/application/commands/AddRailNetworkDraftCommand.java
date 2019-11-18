package com.ralfhenze.rms.railnetworkplanning.application.commands;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class AddRailNetworkDraftCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public AddRailNetworkDraftCommand(
        final RailNetworkDraftRepository railNetworkDraftRepository
    ) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public Optional<RailNetworkDraftId> addRailNetworkDraft() {
        RailNetworkDraft draft = new RailNetworkDraft();

        Optional<RailNetworkDraft> persistedDraft = railNetworkDraftRepository.persist(draft);

        if (persistedDraft.isPresent()) {
            return persistedDraft.get().getId();
        } else {
            return Optional.empty();
        }
    }
}
