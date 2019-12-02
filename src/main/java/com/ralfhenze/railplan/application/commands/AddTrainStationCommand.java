package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;

import java.util.Optional;

public class AddTrainStationCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    public AddTrainStationCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public Optional<TrainStation> addTrainStation(
        final String railNetworkDraftId,
        final String stationName,
        final double latitude,
        final double longitude
    ) throws ValidationException {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        if (draft.isPresent()) {
            final var validation = new Validation();
            final var name = validation.catchErrors(() -> new TrainStationName(stationName));
            final var location = validation.catchErrors(
                () -> new GeoLocationInGermany(latitude, longitude)
            );
            validation.throwExceptionIfInvalid();

            final var updatedDraft = draft.get().withNewStation(name, location);

            draftRepository.persist(updatedDraft);

            return updatedDraft.getStations().getLastOptional();
        }

        return Optional.empty();
    }
}
