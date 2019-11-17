package com.ralfhenze.rms.railnetworkplanning.application;

import com.ralfhenze.rms.railnetworkplanning.application.commands.AddStationCommand;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.berlinHbfName;
import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.berlinHbfPos;
import static org.mockito.Mockito.*;

class CommandsTest {

    @Test
    void should_persist_added_station() {
        RailNetworkDraftId id = new RailNetworkDraftId("1");
        RailNetworkDraftRepository draftRepository = mock(RailNetworkDraftRepository.class);

        when(draftRepository.getRailNetworkDraftOfId(any()))
            .thenReturn(Optional.of(new RailNetworkDraft(id)));

        AddStationCommand addStationCommand = new AddStationCommand(draftRepository);
        addStationCommand.addStation(
            "1",
            berlinHbfName.getName(),
            berlinHbfPos.getLocation().getLatitude(),
            berlinHbfPos.getLocation().getLongitude()
        );

        verify(draftRepository).persist(any());
    }
}
