package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class DraftsController {

    private final Queries queries;
    private final RailNetworkDraftRepository draftRepository;
    private final AddRailNetworkDraftCommand addRailNetworkDraftCommand;
    private final DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand;
    private final ReleaseRailNetworkCommand releaseRailNetworkCommand;

    @Autowired
    public DraftsController(
        final Queries queries,
        final RailNetworkDraftRepository draftRepository,
        final AddRailNetworkDraftCommand addRailNetworkDraftCommand,
        final DeleteRailNetworkDraftCommand deleteRailNetworkDraftCommand,
        final ReleaseRailNetworkCommand releaseRailNetworkCommand
    ) {
        this.queries = queries;
        this.draftRepository = draftRepository;
        this.addRailNetworkDraftCommand = addRailNetworkDraftCommand;
        this.deleteRailNetworkDraftCommand = deleteRailNetworkDraftCommand;
        this.releaseRailNetworkCommand = releaseRailNetworkCommand;
    }

    /**
     * Provides a list of all Drafts.
     */
    @GetMapping("/drafts")
    public String drafts(Model model) {
        model.addAttribute("draftIds", queries.getAllDraftIds());

        return "drafts";
    }

    /**
     * Creates a new Draft and redirects to it.
     */
    @GetMapping("/drafts/new")
    public String addDraft() {
        final var draftId = addRailNetworkDraftCommand
            .addRailNetworkDraft().get()
            .getId().get().toString();

        return "redirect:/drafts/" + draftId;
    }

    /**
     * Deletes an existing Draft and redirects to /drafts.
     */
    @GetMapping("/drafts/{currentDraftId}/delete")
    public String deleteDraft(@PathVariable String currentDraftId) {
        deleteRailNetworkDraftCommand
            .deleteRailNetworkDraft(currentDraftId);

        return "redirect:/drafts";
    }

    /**
     * Shows a form to release a Draft.
     */
    @GetMapping("/drafts/{currentDraftId}/release")
    public String showDraftReleaseForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withShowReleaseForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
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

            return new DraftsView(currentDraftId, draftRepository, queries)
                .withShowReleaseForm(true)
                .withReleaseErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }
    }
}
