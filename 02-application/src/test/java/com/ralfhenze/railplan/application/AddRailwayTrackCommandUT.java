package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;

import static com.ralfhenze.railplan.application.TestData.berlinHbfName;
import static com.ralfhenze.railplan.application.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfPos;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddRailwayTrackCommandUT {

    @Test
    public void persistsAddedTrack() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var command = new AddRailwayTrackCommand(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos);
        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(draft);

        command.addRailwayTrack("1", "1", "2");

        verify(draftRepository).persist(any());
    }
}
