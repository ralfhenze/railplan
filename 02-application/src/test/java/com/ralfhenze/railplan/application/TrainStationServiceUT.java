package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.HAMBURG_HBF;
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
        final var networkRepository = mock(RailNetworkRepository.class);
        final var trainStationService = new TrainStationService(networkRepository);
        given(networkRepository.getRailNetworkOfId(any()))
            .willReturn(new RailNetwork());

        trainStationService.addStationToNetwork(
            new AddTrainStationCommand(
                "1",
                BERLIN_HBF.getName(),
                BERLIN_HBF.getLatitude(),
                BERLIN_HBF.getLongitude()
            )
        );

        verify(networkRepository).persist(any());
    }

    @Test
    public void returnsStationValidationErrorsIfInvalid() {
        final var networkRepository = mock(RailNetworkRepository.class);
        final var trainStationService = new TrainStationService(networkRepository);
        given(networkRepository.getRailNetworkOfId(any()))
            .willReturn(new RailNetwork());

        List<ValidationError> validationErrors = List.of();
        try {
            trainStationService.addStationToNetwork(
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
        final var networkRepository = mock(RailNetworkRepository.class);
        final var trainStationService = new TrainStationService(networkRepository);
        final var network = new RailNetwork().addStations(BERLIN_HBF);
        final var updatedNetworkCaptor = ArgumentCaptor.forClass(RailNetwork.class);
        given(networkRepository.getRailNetworkOfId(any()))
            .willReturn(network);

        trainStationService.updateStationOfNetwork(
            new UpdateTrainStationCommand(
                "1",
                1,
                HAMBURG_HBF.getName(),
                HAMBURG_HBF.getLatitude(),
                HAMBURG_HBF.getLongitude()
            )
        );

        verify(networkRepository).persist(updatedNetworkCaptor.capture());
        final var updatedStation = updatedNetworkCaptor.getValue().getStations().getAny();
        assertThat(updatedStation.getName().getName()).isEqualTo(HAMBURG_HBF.getName());
        assertThat(updatedStation.getLocation().getLatitude()).isEqualTo(HAMBURG_HBF.getLatitude());
        assertThat(updatedStation.getLocation().getLongitude()).isEqualTo(HAMBURG_HBF.getLongitude());
    }

    @Test
    public void deletesStationAndPersistsUpdatedNetwork() {
        final var networkRepository = mock(RailNetworkRepository.class);
        final var trainStationService = new TrainStationService(networkRepository);
        final var network = new RailNetwork().addStations(BERLIN_HBF);
        final var updatedNetworkCaptor = ArgumentCaptor.forClass(RailNetwork.class);
        given(networkRepository.getRailNetworkOfId(any()))
            .willReturn(network);

        trainStationService.deleteStationFromNetwork(
            new DeleteTrainStationCommand("1", 1)
        );

        verify(networkRepository).persist(updatedNetworkCaptor.capture());
        assertThat(updatedNetworkCaptor.getValue().getStations()).isEmpty();
    }
}
