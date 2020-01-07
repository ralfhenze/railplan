package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to add a new TrainStation to a RailNetworkDraft
 */
public class TrainStationService implements ApplicationService {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the application service
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public TrainStationService(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Executes AddTrainStationCommand
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public RailNetworkDraft addStationToDraft(final AddTrainStationCommand command) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(command.getDraftId()));

        final var updatedDraft = draft.withNewStation(
            new TrainStationName(command.getStationName()),
            new GeoLocationInGermany(command.getLatitude(), command.getLongitude())
        );

        if (updatedDraft.isValid()) {
            draftRepository.persist(updatedDraft);
        }

        return updatedDraft;
    }
}
