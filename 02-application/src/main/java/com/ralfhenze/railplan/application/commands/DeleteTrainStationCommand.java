package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to delete a TrainStation from a RailNetworkDraft.
 */
public class DeleteTrainStationCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the command.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public DeleteTrainStationCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Deletes given TrainStation from given RailNetworkDraft.
     *
     * @throws EntityNotFoundException if TrainStation with stationId or RailNetworkDraft
     *                                 with draftId does not exist
     */
    public void deleteTrainStation(final String stationId, final String draftId) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        final var updatedDraft = draft
            .withoutStation(new TrainStationId(stationId));

        draftRepository.persist(updatedDraft);
    }
}
