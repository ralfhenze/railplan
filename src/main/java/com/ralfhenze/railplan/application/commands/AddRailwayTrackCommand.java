package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class AddRailwayTrackCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public AddRailwayTrackCommand(final RailNetworkDraftRepository railNetworkDraftRepository) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public Optional<RailwayTrack> addRailwayTrack(
        final String railNetworkDraftId,
        final String firstStationId,
        final String secondStationId
    ) {
        final var draft = railNetworkDraftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        if (draft.isPresent()) {
            final var updatedDraft = draft.get().withNewTrack(
                new TrainStationId(firstStationId),
                new TrainStationId(secondStationId)
            );

            railNetworkDraftRepository.persist(updatedDraft);

            return updatedDraft.getTracks().getLastOptional();
        }

        return Optional.empty();
    }
}
