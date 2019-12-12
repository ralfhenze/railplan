package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class DeleteRailwayTrackCommandUT {

    @Test
    public void cannotBeConstructedWithNullArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
            new DeleteRailwayTrackCommand(null)
        );
    }

    @Test
    public void deletesTrackAndPersistsUpdatedDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new DeleteRailwayTrackCommand(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);
        final var track = draft.getTracks().getFirstOptional().get();
        final var updatedDraftCaptor = ArgumentCaptor.forClass(RailNetworkDraft.class);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        command.deleteRailwayTrack(
            "1",
            track.getFirstStationId().toString(),
            track.getSecondStationId().toString()
        );

        then(draftRepository).should().persist(updatedDraftCaptor.capture());
        assertThat(updatedDraftCaptor.getValue().getTracks()).isEmpty();
    }
}
