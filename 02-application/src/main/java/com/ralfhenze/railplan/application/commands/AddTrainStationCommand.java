package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to add a new TrainStation to a RailNetworkDraft
 */
public class AddTrainStationCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the command
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public AddTrainStationCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Adds a new TrainStation to given RailNetworkDraft
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public RailNetworkDraft addTrainStation(
        final String draftId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        final var updatedDraft = draft.withNewStation(
            new TrainStationName(stationName),
            new GeoLocationInGermany(latitude, longitude)
        );

        if (updatedDraft.isValid()) {
            draftRepository.persist(updatedDraft);
        }

        return updatedDraft;
    }
}
