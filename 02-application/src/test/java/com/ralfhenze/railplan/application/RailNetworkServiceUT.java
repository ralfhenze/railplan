package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailNetworkCommand;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RailNetworkServiceUT {

    @Test
    public void persistsCreatedNetwork() {
        final var networkRepository = mock(RailNetworkRepository.class);
        final var railNetworkService = new RailNetworkService(networkRepository);

        railNetworkService.addNetwork();

        verify(networkRepository).persist(any());
    }

    @Test
    public void deletesNetwork() {
        final var networkRepository = mock(RailNetworkRepository.class);
        final var railNetworkService = new RailNetworkService(networkRepository);
        final var networkIdCaptor = ArgumentCaptor.forClass(RailNetworkId.class);
        final var networkId = "1";

        railNetworkService.deleteNetwork(
            new DeleteRailNetworkCommand(networkId)
        );

        verify(networkRepository).deleteRailNetworkOfId(networkIdCaptor.capture());
        assertThat(networkIdCaptor.getValue().toString()).isEqualTo(networkId);
    }
}
