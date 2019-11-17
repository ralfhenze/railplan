package com.ralfhenze.rms.railnetworkplanning.application;

import com.ralfhenze.rms.railnetworkplanning.application.commands.AddStationCommand;
import com.ralfhenze.rms.railnetworkplanning.application.commands.CreateRailNetworkDraftCommand;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.berlinHbfName;
import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.berlinHbfPos;
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
            berlinHbfPos.getLocation().getLatitude(),
            berlinHbfPos.getLocation().getLongitude()
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
}
