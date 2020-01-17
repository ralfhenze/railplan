package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_LAT;
import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_LNG;
import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_NAME;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_LAT;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_LNG;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_NAME;
import static org.assertj.core.api.Assertions.assertThat;
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
            .withNewStation(BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG)
            .withNewStation(HAMBURG_HBF_NAME, HAMBURG_HBF_LAT, HAMBURG_HBF_LNG);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        railwayTrackService.addTrackByStationId(
            new AddRailwayTrackByStationIdCommand("1", 1, 2)
        );

        verify(draftRepository).persist(any());
    }

    @Test
    public void deletesTrackAndPersistsUpdatedDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var railwayTrackService = new RailwayTrackService(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG)
            .withNewStation(HAMBURG_HBF_NAME, HAMBURG_HBF_LAT, HAMBURG_HBF_LNG)
            .withNewTrack(BERLIN_HBF_NAME, HAMBURG_HBF_NAME);
        final var track = draft.getTracks().getFirstOptional().get();
        final var updatedDraftCaptor = ArgumentCaptor.forClass(RailNetworkDraft.class);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        railwayTrackService.deleteTrackFromDraft(
            new DeleteRailwayTrackCommand(
                "1",
                track.getFirstStationId().getId(),
                track.getSecondStationId().getId()
            )
        );

        verify(draftRepository).persist(updatedDraftCaptor.capture());
        assertThat(updatedDraftCaptor.getValue().getTracks()).isEmpty();
    }
}
