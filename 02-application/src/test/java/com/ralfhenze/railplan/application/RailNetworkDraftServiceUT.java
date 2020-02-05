package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkDraftRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    public void deletesDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var railNetworkDraftService = new RailNetworkDraftService(draftRepository);
        final var draftIdCaptor = ArgumentCaptor.forClass(RailNetworkDraftId.class);
        final var draftId = "1";

        railNetworkDraftService.deleteDraft(
            new DeleteRailNetworkDraftCommand(draftId)
        );

        verify(draftRepository).deleteRailNetworkDraftOfId(draftIdCaptor.capture());
        assertThat(draftIdCaptor.getValue().toString()).isEqualTo(draftId);
    }
}
