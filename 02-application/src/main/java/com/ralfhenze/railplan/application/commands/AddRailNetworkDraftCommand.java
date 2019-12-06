package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class AddRailNetworkDraftCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public AddRailNetworkDraftCommand(
        final RailNetworkDraftRepository railNetworkDraftRepository
    ) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public Optional<RailNetworkDraft> addRailNetworkDraft() {
        RailNetworkDraft draft = new RailNetworkDraft();

        Optional<RailNetworkDraft> persistedDraft = railNetworkDraftRepository.persist(draft);

        return persistedDraft;
    }
}
