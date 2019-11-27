package com.ralfhenze.railplan.application.commands;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.*;

import java.time.LocalDate;
import java.util.Optional;

public class ReleaseRailNetworkCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;
    final private RailNetworkReleaseService railNetworkReleaseService;

    public ReleaseRailNetworkCommand(
        final RailNetworkDraftRepository railNetworkDraftRepository,
        final ReleasedRailNetworkRepository railNetworkRepository
    ) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
        this.railNetworkReleaseService = new RailNetworkReleaseService(railNetworkRepository);
    }

    public Optional<ReleasedRailNetworkId> releaseRailNetworkDraft(
        final String railNetworkDraftId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final Optional<RailNetworkDraft> draft = railNetworkDraftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        if (draft.isPresent()) {
            final Optional<ReleasedRailNetwork> releasedRailNetwork = railNetworkReleaseService
                .release(
                    draft.get(),
                    new ValidityPeriod(startDate, endDate)
                );

            if (releasedRailNetwork.isPresent()) {
                // TODO: emit RailNetworkReleased event

                return releasedRailNetwork.get().getId();
            }
        }

        return Optional.empty();
    }
}