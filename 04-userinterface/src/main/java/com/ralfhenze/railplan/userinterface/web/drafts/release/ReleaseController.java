package com.ralfhenze.railplan.userinterface.web.drafts.release;

import com.ralfhenze.railplan.application.ReleasedRailNetworkService;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
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
public class ReleaseController {

    private final RailNetworkDraftRepository draftRepository;
    private final ReleasedRailNetworkService releasedRailNetworkService;

    @Autowired
    public ReleaseController(
        final RailNetworkDraftRepository draftRepository,
        final ReleasedRailNetworkService releasedRailNetworkService
    ) {
        this.draftRepository = draftRepository;
        this.releasedRailNetworkService = releasedRailNetworkService;
    }

    /**
     * Shows a form to release a Draft.
     */
    @GetMapping("/drafts/{currentDraftId}/release")
    public String showDraftReleaseForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        return new ReleaseView(currentDraftId, draftRepository)
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
            final var network = releasedRailNetworkService.releaseDraft(
                new ReleaseRailNetworkCommand(
                    currentDraftId,
                    LocalDate.parse(periodDto.getStartDate()),
                    LocalDate.parse(periodDto.getEndDate())
                )
            );

            return "redirect:/networks/" + network.getId().get();

        } catch (ValidationException exception) {
            return new ReleaseView(currentDraftId, draftRepository)
                .withValidationException(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }
    }
}
