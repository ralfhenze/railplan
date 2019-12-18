package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationId;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;

import java.util.Optional;

public class AddRailwayTrackCommand implements Command {

    final private RailNetworkDraftRepository draftRepository;

    public AddRailwayTrackCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public Optional<RailwayTrack> addRailwayTrack(
        final String railNetworkDraftId,
        final String firstStationId,
        final String secondStationId
    ) throws ValidationException {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        final var updatedDraft = draft.withNewTrack(
            new TrainStationId(firstStationId),
            new TrainStationId(secondStationId)
        );

        draftRepository.persist(updatedDraft);

        return updatedDraft.getTracks().getLastOptional();
    }

    public RailwayTrack addRailwayTrackByStationName(
        final String railNetworkDraftId,
        final String firstStationName,
        final String secondStationName
    ) throws ValidationException {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        final var updatedDraft = draft.withNewTrack(
            new TrainStationName(firstStationName),
            new TrainStationName(secondStationName)
        );

        draftRepository.persist(updatedDraft);

        return updatedDraft.getTracks().getLast();
    }
}
