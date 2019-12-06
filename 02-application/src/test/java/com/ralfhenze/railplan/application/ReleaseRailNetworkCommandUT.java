package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.junit.Test;

import static com.ralfhenze.railplan.application.TestData.berlinHbfName;
import static com.ralfhenze.railplan.application.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.application.TestData.defaultPeriod;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfPos;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReleaseRailNetworkCommandUT {

    @Test
    public void persistsReleasedNetwork() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var networkRepository = mock(ReleasedRailNetworkRepository.class);
        final var command = new ReleaseRailNetworkCommand(draftRepository, networkRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);
        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(draft);

        command.releaseRailNetworkDraft(
            "1",
            defaultPeriod.getStartDate(),
            defaultPeriod.getEndDate()
        );

        verify(networkRepository).add(any());
    }
}
