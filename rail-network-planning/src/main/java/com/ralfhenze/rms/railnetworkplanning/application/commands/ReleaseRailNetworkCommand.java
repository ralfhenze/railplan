package com.ralfhenze.rms.railnetworkplanning.application.commands;

import com.ralfhenze.rms.railnetworkplanning.domain.*;

import java.time.LocalDate;
import java.util.Optional;

public class ReleaseRailNetworkCommand implements Command {

    final private RailNetworkDraftRepository railNetworkDraftRepository;
    final private RailNetworkReleaseService railNetworkReleaseService;

    public ReleaseRailNetworkCommand(
        final RailNetworkDraftRepository railNetworkDraftRepository,
        final RailNetworkRepository railNetworkRepository
    ) {
        this.railNetworkDraftRepository = railNetworkDraftRepository;
        this.railNetworkReleaseService = new RailNetworkReleaseService(railNetworkRepository);
    }

    public Optional<RailNetworkId> releaseRailNetworkDraft(
        final String railNetworkDraftId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final Optional<RailNetworkDraft> draft = railNetworkDraftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(railNetworkDraftId));

        if (draft.isPresent()) {
            final Optional<RailNetwork> releasedRailNetwork = railNetworkReleaseService
                .release(
                    draft.get(),
                    new RailNetworkPeriod(startDate, endDate)
                );

            if (releasedRailNetwork.isPresent()) {
                // TODO: emit RailNetworkReleased event

                return releasedRailNetwork.get().getId();
            }
        }

        return Optional.empty();
    }
}
