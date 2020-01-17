package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.HAMBURG_HBF;
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
        final var draft = RailNetworkDraft.of(BERLIN_HBF, HAMBURG_HBF);
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
        final var draft = RailNetworkDraft.of(BERLIN_HBF, HAMBURG_HBF)
            .withNewTrack(BERLIN_HBF.getName(), HAMBURG_HBF.getName());
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
