package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RailNetworkDraftServiceUT {

    @Test
    public void persistsCreatedDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var railNetworkDraftService = new RailNetworkDraftService(draftRepository);

        railNetworkDraftService.addDraft();

        verify(draftRepository).persist(any());
    }
}
