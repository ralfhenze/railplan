package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.DeleteRailNetworkCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;

import java.util.Optional;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to create and delete RailNetworks.
 */
public class RailNetworkService implements ApplicationService {

    final private RailNetworkRepository networkRepository;

    /**
     * Constructs the Network service.
     *
     * @throws IllegalArgumentException if networkRepository is null
     */
    public RailNetworkService(final RailNetworkRepository networkRepository) {
        this.networkRepository = ensureNotNull(networkRepository, "Network Repository");
    }

    /**
     * Creates a new empty Network.
     */
    public Optional<RailNetwork> addNetwork() {
        return networkRepository.persist(new RailNetwork());
    }

    /**
     * Deletes an existing Network.
     *
     * @throws ValidationException if networkId is not valid
     * @throws EntityNotFoundException if RailNetwork with networkId does not exist
     */
    public void deleteNetwork(final DeleteRailNetworkCommand command) {
        final var networkId = new RailNetworkId(command.getNetworkId());
        networkRepository.deleteRailNetworkOfId(networkId);
    }
}
