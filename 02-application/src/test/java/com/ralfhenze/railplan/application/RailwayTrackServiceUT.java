package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;

import static com.ralfhenze.railplan.application.TestData.berlinHbfName;
import static com.ralfhenze.railplan.application.TestData.berlinHbfPos;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.application.TestData.hamburgHbfPos;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RailwayTrackServiceUT {

    @Test
    public void persistsAddedTrack() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var railwayTrackService = new RailwayTrackService(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        railwayTrackService.addTrackByStationId(
            new AddRailwayTrackByStationIdCommand("1", "1", "2")
        );

        verify(draftRepository).persist(any());
    }
}
