package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_LAT;
import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_LNG;
import static com.ralfhenze.railplan.application.TestData.BERLIN_HBF_NAME;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_LAT;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_LNG;
import static com.ralfhenze.railplan.application.TestData.HAMBURG_HBF_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrainStationServiceUT {

    @Test
    public void cannotBeConstructedWithNullArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
            new TrainStationService(null)
        );
    }

    @Test
    public void persistsAddedStation() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var trainStationService = new TrainStationService(draftRepository);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(new RailNetworkDraft());

        trainStationService.addStationToDraft(
            new AddTrainStationCommand("1", BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG)
        );

        verify(draftRepository).persist(any());
    }

    @Test
    public void returnsStationValidationErrorsIfInvalid() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var trainStationService = new TrainStationService(draftRepository);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(new RailNetworkDraft());

        List<ValidationError> validationErrors = List.of();
        try {
            trainStationService.addStationToDraft(
                new AddTrainStationCommand(
                    "1",
                    "Be", // 1. too short + 2. doesn't match Regex
                    0,    // 3. out of range
                    0     // 4. out of range
                )
            );
        } catch (ValidationException exception) {
            validationErrors = exception.getValidationErrors();
        }

        assertThat(validationErrors).hasSize(4);
    }

    @Test
    public void persistsUpdatedStation() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var trainStationService = new TrainStationService(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG);
        final var updatedDraftCaptor = ArgumentCaptor.forClass(RailNetworkDraft.class);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        trainStationService.updateStationOfDraft(
            new UpdateTrainStationCommand("1", 1, HAMBURG_HBF_NAME, HAMBURG_HBF_LAT, HAMBURG_HBF_LNG)
        );

        verify(draftRepository).persist(updatedDraftCaptor.capture());
        final var updatedStation = updatedDraftCaptor.getValue().getStations().getAny();
        assertThat(updatedStation.getName().getName()).isEqualTo(HAMBURG_HBF_NAME);
        assertThat(updatedStation.getLocation().getLatitude()).isEqualTo(HAMBURG_HBF_LAT);
        assertThat(updatedStation.getLocation().getLongitude()).isEqualTo(HAMBURG_HBF_LNG);
    }

    @Test
    public void deletesStationAndPersistsUpdatedDraft() {
        final var draftRepository = mock(RailNetworkDraftRepository.class);
        final var trainStationService = new TrainStationService(draftRepository);
        final var draft = new RailNetworkDraft()
            .withNewStation(BERLIN_HBF_NAME, BERLIN_HBF_LAT, BERLIN_HBF_LNG);
        final var updatedDraftCaptor = ArgumentCaptor.forClass(RailNetworkDraft.class);
        given(draftRepository.getRailNetworkDraftOfId(any()))
            .willReturn(draft);

        trainStationService.deleteStationFromDraft(
            new DeleteTrainStationCommand("1", 1)
        );

        verify(draftRepository).persist(updatedDraftCaptor.capture());
        assertThat(updatedDraftCaptor.getValue().getStations()).isEmpty();
    }
}
