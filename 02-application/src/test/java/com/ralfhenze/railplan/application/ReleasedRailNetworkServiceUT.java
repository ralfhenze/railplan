package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.junit.Test;

import java.util.Optional;

import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_LAT;
import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_LNG;
import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_NAME;
import static com.ralfhenze.railplan.application.TestData.DEFAULT_PERIOD;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_LAT;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_LNG;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_NAME;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReleasedRailNetworkServiceUT {

    @Test
    public void cannotBeConstructedWithNullArguments() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
            new ReleasedRailNetworkService(null, null)
        );
    }

    @Test
    public void persistsReleasedNetwork() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var networkRepository = mock(ReleasedRailNetworkRepository.class);
        final var releasedRailNetworkService = new ReleasedRailNetworkService(
            draftRepository, networkRepository
        );
        final var draft = new RailNetworkDraft()
            .withNewStation(BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG)
            .withNewStation(HAMBURG_HBF_NAME, HAMBURG_HBF_LAT, HAMBURG_HBF_LNG)
            .withNewTrack(BERLIN_HBF_NAME, HAMBURG_HBF_NAME);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);
        final var network = mock(ReleasedRailNetwork.class);
        given(network.getId())
            .willReturn(Optional.of(new ReleasedRailNetworkId("1")));
        given(networkRepository.add(any()))
            .willReturn(network);

        releasedRailNetworkService.releaseDraft(
            new ReleaseRailNetworkCommand(
                "1",
                DEFAULT_PERIOD.getStartDate(),
                DEFAULT_PERIOD.getEndDate()
            )
        );

        verify(networkRepository).add(any());
    }
}
