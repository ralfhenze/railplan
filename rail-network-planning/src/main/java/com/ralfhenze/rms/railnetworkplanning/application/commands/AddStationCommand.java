package com.ralfhenze.rms.railnetworkplanning.application.commands;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;

import java.util.Optional;

public class AddStationCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;

    public AddStationCommand(final RailNetworkDraftRepository railNetworkDraftRepository) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
    }

    public void addStation(
        final String railNetworkDraftId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        final Optional<RailNetworkDraft> draft = railNetworkDraftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        if (draft.isPresent()) {
            final RailNetworkDraft updatedDraft = draft.get().withNewStation(
                new TrainStationName(stationName),
                new GeoLocationInGermany(latitude, longitude)
            );

            railNetworkDraftRepository.persist(updatedDraft);
        }
    }
}
