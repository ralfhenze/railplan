package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.TestData.berlinHbfName;
import static com.ralfhenze.railplan.domain.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.domain.TestData.defaultPeriod;
import static com.ralfhenze.railplan.domain.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.domain.TestData.hamburgHbfPos;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandsUT {

    @Test
    void persistsAddedTrack() {
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
    void persistsCreatedDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddRailNetworkDraftCommand(draftRepository);

        command.addRailNetworkDraft();

        verify(draftRepository).persist(any());
    }

    @Test
    void persistsReleasedNetwork() {
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
