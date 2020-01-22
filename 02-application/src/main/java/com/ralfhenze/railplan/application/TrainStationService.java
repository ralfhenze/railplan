package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to add, update and delete Stations.
 */
public class TrainStationService implements ApplicationService {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the Station service.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public TrainStationService(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Adds a new Station to an existing Draft.
     *
     * @throws ValidationException if draftId, stationName or coordinates are not valid
     * @throws EntityNotFoundException if RailNetworkDraft with draftId does not exist
     */
    public void addStationToDraft(final AddTrainStationCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var updatedDraft = draft.addStation(
            command.getStationName(),
            command.getLatitude(),
            command.getLongitude()
        );

        draftRepository.persist(updatedDraft);
    }

    /**
     * Updates an existing Station of an existing Draft.
     *
     * @throws ValidationException if draftId, stationName or coordinates are not valid
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation
     *                                 with stationId does not exist
     */
    public void updateStationOfDraft(final UpdateTrainStationCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var updatedDraft = draft.updateStation(
            command.getStationId(),
            command.getStationName(),
            command.getLatitude(),
            command.getLongitude()
        );

        draftRepository.persist(updatedDraft);
    }

    /**
     * Deletes an existing Station from an existing Draft.
     *
     * @throws ValidationException if draftId or stationId is not valid
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation
     *                                 with stationId does not exist
     */
    public void deleteStationFromDraft(final DeleteTrainStationCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var updatedDraft = draft.deleteStation(command.getStationId());

        draftRepository.persist(updatedDraft);
    }
}
