package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to add, update and delete Stations.
 */
public class TrainStationService implements ApplicationService {

    private final RailNetworkRepository networkRepository;

    /**
     * Constructs the Station service.
     *
     * @throws IllegalArgumentException if networkRepository is null
     */
    public TrainStationService(final RailNetworkRepository networkRepository) {
        this.networkRepository = ensureNotNull(networkRepository, "Network Repository");
    }

    /**
     * Adds a new Station to an existing Network.
     *
     * @throws ValidationException if networkId, stationName or coordinates are not valid
     * @throws EntityNotFoundException if RailNetwork with networkId does not exist
     */
    public void addStationToNetwork(final AddTrainStationCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        final var network = networkRepository.getRailNetworkOfId(networkId);

        final var updatedNetwork = network.addStation(
            command.getStationName(),
            command.getLatitude(),
            command.getLongitude()
        );

        networkRepository.persist(updatedNetwork);
    }

    /**
     * Updates an existing Station of an existing Network.
     *
     * @throws ValidationException if networkId, stationName or coordinates are not valid
     * @throws EntityNotFoundException if RailNetwork with networkId or TrainStation
     *                                 with stationId does not exist
     */
    public void updateStationOfNetwork(final UpdateTrainStationCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        final var network = networkRepository.getRailNetworkOfId(networkId);

        final var updatedNetwork = network.updateStation(
            command.getStationId(),
            command.getStationName(),
            command.getLatitude(),
            command.getLongitude()
        );

        networkRepository.persist(updatedNetwork);
    }

    /**
     * Deletes an existing Station from an existing Network.
     *
     * @throws ValidationException if networkId or stationId is not valid
     * @throws EntityNotFoundException if RailNetwork with networkId or TrainStation
     *                                 with stationId does not exist
     */
    public void deleteStationFromNetwork(final DeleteTrainStationCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        final var network = networkRepository.getRailNetworkOfId(networkId);

        final var updatedNetwork = network.deleteStation(command.getStationId());

        networkRepository.persist(updatedNetwork);
    }
}
