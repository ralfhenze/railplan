package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
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
        final var networkRepository = mock(RailNetworkRepository.class);
        final var railwayTrackService = new RailwayTrackService(networkRepository);
        final var network = new RailNetwork().addStations(BERLIN_HBF, HAMBURG_HBF);
        given(networkRepository.getRailNetworkOfId(any()))
            .willReturn(network);

        railwayTrackService.addTrackByStationId(
            new AddRailwayTrackByStationIdCommand("1", 1, 2)
        );

        verify(networkRepository).persist(any());
    }

    @Test
    public void deletesTrackAndPersistsUpdatedNetwork() {
        final var networkRepository = mock(RailNetworkRepository.class);
        final var railwayTrackService = new RailwayTrackService(networkRepository);
        final var network = new RailNetwork()
            .addStations(BERLIN_HBF, HAMBURG_HBF)
            .addTrackBetween(BERLIN_HBF, HAMBURG_HBF);
        final var track = network.getTracks().getFirstOptional().get();
        final var updatedNetworkCaptor = ArgumentCaptor.forClass(RailNetwork.class);
        given(networkRepository.getRailNetworkOfId(any()))
            .willReturn(network);

        railwayTrackService.deleteTrackFromNetwork(
            new DeleteRailwayTrackCommand(
                "1",
                track.getFirstStationId().getId(),
                track.getSecondStationId().getId()
            )
        );

        verify(networkRepository).persist(updatedNetworkCaptor.capture());
        assertThat(updatedNetworkCaptor.getValue().getTracks()).isEmpty();
    }
}
