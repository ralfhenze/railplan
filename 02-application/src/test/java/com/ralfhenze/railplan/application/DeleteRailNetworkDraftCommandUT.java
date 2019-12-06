package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class DeleteRailNetworkDraftCommandUT {

    @Test
    public void cannotBeConstructedWithNullArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
            new DeleteRailNetworkDraftCommand(null)
        );
    }

    @Test
    public void deletesDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new DeleteRailNetworkDraftCommand(draftRepository);
        final var draftIdCaptor = ArgumentCaptor.forClass(RailNetworkDraftId.class);
        final var draftId = "1";

        command.deleteRailNetworkDraft(draftId);

        then(draftRepository).should().deleteRailNetworkDraftOfId(draftIdCaptor.capture());
        assertThat(draftIdCaptor.getValue().toString()).isEqualTo(draftId);
    }
}
