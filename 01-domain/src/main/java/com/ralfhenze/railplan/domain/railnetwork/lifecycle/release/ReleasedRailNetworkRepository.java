package com.ralfhenze.railplan.domain.railnetwork.lifecycle.release;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.Repository;

import java.util.Optional;

public interface ReleasedRailNetworkRepository extends Repository {

    /**
     * Loads a ReleasedRailNetwork from persistence mechanism.
     *
     * @throws EntityNotFoundException if ReleasedRailNetwork with networkId does not exist
     */
    ReleasedRailNetwork getReleasedRailNetworkOfId(final ReleasedRailNetworkId networkId);

    Optional<ReleasedRailNetwork> getLastReleasedRailNetwork();
    ReleasedRailNetwork add(final ReleasedRailNetwork railNetwork);
}
