package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class AddTrainStationCommandUT {

    @Test
    void persistsAddedStation() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddTrainStationCommand(draftRepository);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(Optional.of(new RailNetworkDraft()));

        command.addTrainStation(
            "1",
            berlinHbfName.getName(),
            berlinHbfPos.getLatitude(),
            berlinHbfPos.getLongitude()
        );

        then(draftRepository).should().persist(any());
    }

    @Test
    void returnsStationValidationErrorsIfInvalid() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddTrainStationCommand(draftRepository);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(Optional.of(new RailNetworkDraft()));

        var numberOfErrorMessages = 0;
        try {
            command.addTrainStation(
                "1",
                "Be", // 1. too short
                0,    // 2. out of range
                0     // 3. out of range
            );
        } catch (ValidationException exception) {
            numberOfErrorMessages = exception.getErrorMessages().size();
        }

        assertThat(numberOfErrorMessages).isEqualTo(3);
    }
}
