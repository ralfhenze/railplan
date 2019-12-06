package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.Validation;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStation;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.railplan.domain.railnetwork.elements.TrainStationName;

import static com.ralfhenze.railplan.domain.common.Preconditions.ensureNotNull;

/**
 * A command to add a new Train Station to a Rail Network Draft
 */
public class AddTrainStationCommand implements Command {

    private final RailNetworkDraftRepository draftRepository;

    /**
     * Constructs the command
     *
     * @throws IllegalArgumentException if draftRepository is null
     */
    public AddTrainStationCommand(final RailNetworkDraftRepository draftRepository) {
        this.draftRepository = ensureNotNull(draftRepository, "Draft Repository");
    }

    /**
     * Adds a new Train Station to given Rail Network Draft
     *
     * @throws EntityNotFoundException if Rail Network Draft with draftId does not exist
     * @throws ValidationException if stationName or latitude or longitude is invalid
     */
    public TrainStation addTrainStation(
        final String draftId,
        final String stationName,
        final double latitude,
        final double longitude
    ) {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(draftId));

        final var validation = new Validation();
        final var name = validation.catchErrors(() -> new TrainStationName(stationName));
        final var location = validation.catchErrors(
            () -> new GeoLocationInGermany(latitude, longitude)
        );
        validation.throwExceptionIfInvalid();

        final var updatedDraft = draft
            .withNewStation(name, location);

        draftRepository.persist(updatedDraft);

        return updatedDraft.getStations().getLast();
    }
}
