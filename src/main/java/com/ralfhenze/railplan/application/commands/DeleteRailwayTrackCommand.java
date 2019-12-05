package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.application.Command;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to delete a RailwayTrack from a RailNetworkDraft.
 */
public class DeleteRailwayTrackCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the command.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public DeleteRailwayTrackCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Deletes RailwayTrack between Stations of given stationIds from given RailNetworkDraft.
     *
     * @throws EntityNotFoundException if TrainStation with stationId or Track or RailNetworkDraft
     *                                 with draftId does not exist
     */
    public void deleteRailwayTrack(
        final String stationId1,
        final String stationId2,
        final String draftId
    ) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        final var updatedDraft = draft
            .withoutTrack(new TrainStationId(stationId1), new TrainStationId(stationId2));

        draftRepository.persist(updatedDraft);
    }
}
