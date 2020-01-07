package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to execute TrainStation commands
 */
public class TrainStationService implements ApplicationService {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the application service.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public TrainStationService(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Executes AddTrainStationCommand.
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

    /**
     * Executes UpdateTrainStationCommand.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public RailNetworkDraft updateStationOfDraft(final UpdateTrainStationCommand command) {
        final var updatedDraft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(command.getDraftId()))
            .withUpdatedStation(
                new TrainStationId(command.getStationId()),
                new TrainStationName(command.getStationName()),
                new GeoLocationInGermany(command.getLatitude(), command.getLongitude())
            );

        if (updatedDraft.isValid()) {
            draftRepository.persist(updatedDraft);
        }

        return updatedDraft;
    }

    /**
     * Executes DeleteTrainStationCommand.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation
     *                                 with stationId does not exist
     */
    public void deleteStationFromDraft(final DeleteTrainStationCommand command) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(command.getDraftId()));

        final var updatedDraft = draft
            .withoutStation(new TrainStationId(command.getStationId()));

        draftRepository.persist(updatedDraft);
    }
}
