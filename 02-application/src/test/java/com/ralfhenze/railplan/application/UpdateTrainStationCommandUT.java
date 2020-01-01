package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.ralfhenze.railplan.application.TestData.berlinHbfName;
import static com.ralfhenze.railplan.application.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfPos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateTrainStationCommandUT {

    @Test
    public void cannotBeConstructedWithNullArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
            new UpdateTrainStationCommand(null)
        );
    }

    @Test
    public void persistsUpdatedStation() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new UpdateTrainStationCommand(draftRepository);
        final var draft = new RailNetworkDraft().withNewStation(berlinHbfName, berlinHbfPos);
        final var updatedDraftCaptor = ArgumentCaptor.forClass(RailNetworkDraft.class);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        command.updateTrainStation(
            "1",
            "1",
            hamburgHbfName.getName(),
            hamburgHbfPos.getLatitude(),
            hamburgHbfPos.getLongitude()
        );

        verify(draftRepository).persist(updatedDraftCaptor.capture());
        final var updatedStation = updatedDraftCaptor.getValue().getStations().getAny();
        assertThat(updatedStation.getName()).isEqualTo(hamburgHbfName);
        assertThat(updatedStation.getLocation()).isEqualTo(hamburgHbfPos);
    }
}
