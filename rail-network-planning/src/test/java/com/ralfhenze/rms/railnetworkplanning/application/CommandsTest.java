package com.ralfhenze.rms.railnetworkplanning.application;

import com.ralfhenze.rms.railnetworkplanning.application.commands.AddStationCommand;
import com.ralfhenze.rms.railnetworkplanning.application.commands.CreateRailNetworkDraftCommand;
import com.ralfhenze.rms.railnetworkplanning.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.mockito.Mockito.*;

class CommandsTest {

    @Test
    void should_persist_added_station() {
        final RailNetworkDraftRepository draftRepository = mock(RailNetworkDraftRepository.class);
        final AddStationCommand addStationCommand = new AddStationCommand(draftRepository);
        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(Optional.of(new RailNetworkDraft()));

        addStationCommand.addStation(
            "1",
            berlinHbfName.getName(),
            berlinHbfPos.getLatitude(),
            berlinHbfPos.getLongitude()
        );

        verify(draftRepository).persist(any());
    }

    @Test
    void should_persist_created_draft() {
        final RailNetworkDraftRepository draftRepository = mock(RailNetworkDraftRepository.class);
        final CreateRailNetworkDraftCommand createDraftCommand = new CreateRailNetworkDraftCommand(draftRepository);

        createDraftCommand.createRailNetworkDraft();

        verify(draftRepository).persist(any());
    }

    @Test
    void should_persist_released_network() {
        final RailNetworkDraftRepository draftRepository = mock(RailNetworkDraftRepository.class);
        final ReleasedRailNetworkRepository networkRepository = mock(ReleasedRailNetworkRepository.class);
        final ReleaseRailNetworkCommand releaseCommand = new ReleaseRailNetworkCommand(draftRepository, networkRepository);
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withConnection(berlinHbfName, hamburgHbfName);
        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(Optional.of(draft));

        releaseCommand.releaseRailNetworkDraft(
            "1",
            defaultPeriod.getStartDate(),
            defaultPeriod.getEndDate()
        );

        verify(networkRepository).add(any());
    }
}
