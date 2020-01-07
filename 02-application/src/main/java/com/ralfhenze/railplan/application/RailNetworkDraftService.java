package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class RailNetworkDraftService implements ApplicationService {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public RailNetworkDraftService(
        final RailNetworkDraftRepository railNetworkDraftRepository
    ) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public Optional<RailNetworkDraft> addDraft() {
        final var draft = new RailNetworkDraft();

        Optional<RailNetworkDraft> persistedDraft = railNetworkDraftRepository.persist(draft);

        return persistedDraft;
    }
}
