package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to update an existing TrainStation of a RailNetworkDraft.
 */
public class UpdateTrainStationCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the command.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public UpdateTrainStationCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Updates an existing TrainStation of given RailNetworkDraft.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public RailNetworkDraft updateTrainStation(
        final String draftId,
        final String stationId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        final var updatedDraft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId))
            .withUpdatedStation(
                new TrainStationId(stationId),
                new TrainStationName(stationName),
                new GeoLocationInGermany(latitude, longitude)
            );

        if (updatedDraft.isValid()) {
            draftRepository.persist(updatedDraft);
        }

        return updatedDraft;
    }
}
