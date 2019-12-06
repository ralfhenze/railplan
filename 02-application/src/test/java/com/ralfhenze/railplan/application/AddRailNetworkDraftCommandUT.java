package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AddRailNetworkDraftCommandUT {

    @Test
    void persistsCreatedDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddRailNetworkDraftCommand(draftRepository);

        command.addRailNetworkDraft();

        verify(draftRepository).persist(any());
    }
}
