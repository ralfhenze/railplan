package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.TestData.*;
import static org.mockito.Mockito.*;

class CommandsUT {

    @Test
    void should_persist_added_track() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddRailwayTrackCommand(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos);
        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(Optional.of(draft));

        command.addRailwayTrack("1", "1", "2");

        verify(draftRepository).persist(any());
    }

    @Test
    void should_persist_created_draft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddRailNetworkDraftCommand(draftRepository);

        command.addRailNetworkDraft();

        verify(draftRepository).persist(any());
    }

    @Test
    void should_persist_released_network() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var networkRepository = mock(ReleasedRailNetworkRepository.class);
        final var command = new ReleaseRailNetworkCommand(draftRepository, networkRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);
        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(Optional.of(draft));

        command.releaseRailNetworkDraft(
            "1",
            defaultPeriod.getStartDate(),
            defaultPeriod.getEndDate()
        );

        verify(networkRepository).add(any());
    }
}
