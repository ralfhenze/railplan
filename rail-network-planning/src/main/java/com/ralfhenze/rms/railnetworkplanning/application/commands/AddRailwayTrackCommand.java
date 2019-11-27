package com.ralfhenze.rms.railnetworkplanning.application.commands;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class AddRailwayTrackCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public AddRailwayTrackCommand(final RailNetworkDraftRepository railNetworkDraftRepository) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public void addRailwayTrack(
        final String railNetworkDraftId,
        final String firstStationId,
        final String secondStationId
    ) {
        final Optional<RailNetworkDraft> draft = railNetworkDraftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        if (draft.isPresent()) {
            final RailNetworkDraft updatedDraft = draft.get().withNewTrack(
                new TrainStationId(firstStationId),
                new TrainStationId(secondStationId)
            );

            railNetworkDraftRepository.persist(updatedDraft);
        }
    }
}
