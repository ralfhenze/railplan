package com.ralfhenze.rms.railnetworkplanning.application.commands;

import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocation;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;

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
                new StationName(stationName),
                new GeoLocationInGermany(new GeoLocation(latitude, longitude))
            );

            railNetworkDraftRepository.persist(updatedDraft);
        }
    }
}
