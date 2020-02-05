package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationNameCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkDraftRepository;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * An application service to add and delete Tracks.
 */
public class RailwayTrackService implements ApplicationService {

    final private RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the Track service.
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public RailwayTrackService(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Adds a new Track (identified by Station IDs) to an existing Draft.
     *
     * @throws ValidationException if draftId or stationIds are not valid or Track
     *                             invariants are violated
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation with
     *                                 firstStationId or secondStationId does not exist
     */
    public void addTrackByStationId(final AddRailwayTrackByStationIdCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var updatedDraft = draft.addTrackBetween(
            command.getFirstStationId(),
            command.getSecondStationId()
        );

        draftRepository.persist(updatedDraft);
    }

    /**
     * Adds a new Track (identified by Station names) to an existing Draft.
     *
     * @throws ValidationException if draftId or stationNames are not valid or Track
     *                             invariants are violated
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation with
     *                                 firstStationName or secondStationName does not exist
     */
    public void addTrackByStationName(final AddRailwayTrackByStationNameCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var updatedDraft = draft.addTrackBetween(
            command.getFirstStationName(),
            command.getSecondStationName()
        );

        draftRepository.persist(updatedDraft);
    }

    /**
     * Deletes an existing Track from an existing Draft.
     *
     * @throws ValidationException if draftId or stationIds are not valid
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation
     *                                 with stationId1 or stationId2 does not exist
     */
    public void deleteTrackFromDraft(final DeleteRailwayTrackCommand command) {
        final var draftId = new RailNetworkDraftId(command.getDraftId());
        final var draft = draftRepository.getRailNetworkDraftOfId(draftId);

        final var updatedDraft = draft.deleteTrackBetween(
            command.getFirstStationId(),
            command.getSecondStationId()
        );

        draftRepository.persist(updatedDraft);
    }
}
