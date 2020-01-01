package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;

import static com.ralfhenze.railplan.application.TestData.berlinHbfName;
import static com.ralfhenze.railplan.application.TestData.berlinHbfPos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTrainStationCommandUT {

    @Test
    public void cannotBeConstructedWithNullArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
            new AddTrainStationCommand(null)
        );
    }

    @Test
    public void persistsAddedStation() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddTrainStationCommand(draftRepository);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(new RailNetworkDraft());

        command.addTrainStation(
            "1",
            berlinHbfName.getName(),
            berlinHbfPos.getLatitude(),
            berlinHbfPos.getLongitude()
        );

        verify(draftRepository).persist(any());
    }

    @Test
    public void returnsStationValidationErrorsIfInvalid() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddTrainStationCommand(draftRepository);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(new RailNetworkDraft());

        final var draft = command.addTrainStation(
            "1",
            "Be", // 1. too short
            0,    // 2. out of range
            0     // 3. out of range
        );

        assertThat(draft.isValid()).isFalse();
    }
}
