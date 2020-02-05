package com.ralfhenze.railplan.domain.railnetwork;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.Repository;

import java.util.Optional;

/**
 * A persistence technology agnostic interface to store and retrieve RailNetworks.
 */
public interface RailNetworkRepository extends Repository {

    /**
     * Loads a RailNetwork from persistence mechanism.
     *
     * @throws EntityNotFoundException if RailNetwork with networkId does not exist
     */
    RailNetwork getRailNetworkOfId(final RailNetworkId networkId);

    /**
     * Stores a RailNetwork in persistence mechanism.
     */
    Optional<RailNetwork> persist(final RailNetwork network);

    /**
     * Deletes a RailNetwork from persistence mechanism.
     *
     * @throws EntityNotFoundException if RailNetwork with networkId does not exist
     */
    void deleteRailNetworkOfId(final RailNetworkId networkId);
}
