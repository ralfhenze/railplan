package com.ralfhenze.rms.railnetworkplanning.application.commands;

import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftRepository;

import java.util.Optional;

public class CreateRailNetworkDraftCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public CreateRailNetworkDraftCommand(
        final RailNetworkDraftRepository railNetworkDraftRepository
    ) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public Optional<RailNetworkDraftId> createRailNetworkDraft() {
        RailNetworkDraft draft = new RailNetworkDraft();

        Optional<RailNetworkDraft> persistedDraft = railNetworkDraftRepository.persist(draft);

        if (persistedDraft.isPresent()) {
            return persistedDraft.get().getId();
        } else {
            return Optional.empty();
        }
    }
}
