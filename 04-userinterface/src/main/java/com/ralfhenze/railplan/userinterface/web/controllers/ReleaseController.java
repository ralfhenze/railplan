package com.ralfhenze.railplan.userinterface.web.controllers;

import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.userinterface.web.DraftsView;
import com.ralfhenze.railplan.userinterface.web.ValidityPeriodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class ReleaseController {

    private final Queries queries;
    private final RailNetworkDraftRepository draftRepository;
    private final ReleaseRailNetworkCommand releaseRailNetworkCommand;

    @Autowired
    public ReleaseController(
        final Queries queries,
        final RailNetworkDraftRepository draftRepository,
        final ReleaseRailNetworkCommand releaseRailNetworkCommand
    ) {
        this.queries = queries;
        this.draftRepository = draftRepository;
        this.releaseRailNetworkCommand = releaseRailNetworkCommand;
    }

    /**
     * Shows a form to release a Draft.
     */
    @GetMapping("/drafts/{currentDraftId}/release")
    public String showDraftReleaseForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        new DraftsView(currentDraftId, draftRepository, queries)
            .withShowReleaseForm(true)
            .addRequiredAttributesTo(model);

        return "stations";
    }

    /**
     * Releases a Draft or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/release")
    public String releaseDraft(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "validityPeriod") ValidityPeriodDto periodDto,
        Model model
    ) {
        try {
            final var networkId = releaseRailNetworkCommand.releaseRailNetworkDraft(
                currentDraftId,
                LocalDate.parse(periodDto.getStartDate()),
                LocalDate.parse(periodDto.getEndDate())
            );

            return "redirect:/networks/" + networkId;

        } catch (ValidationException exception) {

            new DraftsView(currentDraftId, draftRepository, queries)
                .withShowReleaseForm(true)
                .withReleaseErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model);

            return "stations";
        }
    }
}
