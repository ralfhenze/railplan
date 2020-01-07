package com.ralfhenze.railplan.application;

import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationNameCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

public class RailwayTrackService implements ApplicationService {

    final private RailNetworkDraftRepository draftRepository;

    public RailwayTrackService(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public RailNetworkDraft addTrackByStationId(
        final AddRailwayTrackByStationIdCommand command
    ) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(command.getDraftId()));

        final var updatedDraft = draft.withNewTrack(
            new TrainStationId(command.getFirstStationId()),
            new TrainStationId(command.getSecondStationId())
        );

        if (updatedDraft.isValid()) {
            draftRepository.persist(updatedDraft);
        }

        return updatedDraft;
    }

    public RailNetworkDraft addTrackByStationName(
        final AddRailwayTrackByStationNameCommand command
    ) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(command.getDraftId()));

        final var updatedDraft = draft.withNewTrack(
            new TrainStationName(command.getFirstStationName()),
            new TrainStationName(command.getSecondStationName())
        );

        if (updatedDraft.isValid()) {
            draftRepository.persist(updatedDraft);
        }

        return updatedDraft;
    }

    /**
     * Executes DeleteRailwayTrackCommand.
     *
     * @throws EntityNotFoundException if RailNetworkDraft with draftId or TrainStation
     *                                 with stationId1 or stationId2 does not exist
     */
    public void deleteTrackFromDraft(final DeleteRailwayTrackCommand command) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(command.getDraftId()));

        final var updatedDraft = draft
            .withoutTrack(
                new TrainStationId(command.getStationId1()),
                new TrainStationId(command.getStationId2())
            );

        draftRepository.persist(updatedDraft);
    }
}
