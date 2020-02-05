package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationNameCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to add and delete Tracks.
 */
public class RailwayTrackService implements ApplicationService {

    final private RailNetworkRepository networkRepository;

    /**
     * Constructs the Track service.
     *
     * @throws IllegalArgumentException if networkRepository is null
     */
    public RailwayTrackService(final RailNetworkRepository networkRepository) {
        this.networkRepository = ensureNotNull(networkRepository, "Network Repository");
    }

    /**
     * Adds a new Track (identified by Station IDs) to an existing Network.
     *
     * @throws ValidationException if networkId or stationIds are not valid or Track
     *                             invariants are violated
     * @throws EntityNotFoundException if RailNetwork with networkId or TrainStation with
     *                                 firstStationId or secondStationId does not exist
     */
    public void addTrackByStationId(final AddRailwayTrackByStationIdCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        final var network = networkRepository.getRailNetworkOfId(networkId);

        final var updatedNetwork = network.addTrackBetween(
            command.getFirstStationId(),
            command.getSecondStationId()
        );

        networkRepository.persist(updatedNetwork);
    }

    /**
     * Adds a new Track (identified by Station names) to an existing Network.
     *
     * @throws ValidationException if networkId or stationNames are not valid or Track
     *                             invariants are violated
     * @throws EntityNotFoundException if RailNetwork with networkId or TrainStation with
     *                                 firstStationName or secondStationName does not exist
     */
    public void addTrackByStationName(final AddRailwayTrackByStationNameCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        final var network = networkRepository.getRailNetworkOfId(networkId);

        final var updatedNetwork = network.addTrackBetween(
            command.getFirstStationName(),
            command.getSecondStationName()
        );

        networkRepository.persist(updatedNetwork);
    }

    /**
     * Deletes an existing Track from an existing Network.
     *
     * @throws ValidationException if networkId or stationIds are not valid
     * @throws EntityNotFoundException if RailNetwork with networkId or TrainStation
     *                                 with stationId1 or stationId2 does not exist
     */
    public void deleteTrackFromNetwork(final DeleteRailwayTrackCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        final var network = networkRepository.getRailNetworkOfId(networkId);

        final var updatedNetwork = network.deleteTrackBetween(
            command.getFirstStationId(),
            command.getSecondStationId()
        );

        networkRepository.persist(updatedNetwork);
    }
}
