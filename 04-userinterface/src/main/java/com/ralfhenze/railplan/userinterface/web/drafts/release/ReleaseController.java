package com.ralfhenze.railplan.userinterface.web.drafts.release;

import com.ralfhenze.railplan.application.ReleasedRailNetworkService;
import com.ralfhenze.railplan.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

import static com.ralfhenze.railplan.userinterface.web.ControllerHelper.redirectTo;

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
    @GetMapping("/drafts/{draftId}/release")
    @ResponseBody
    public String showDraftReleaseForm(@PathVariable String draftId) {
        return new ReleaseView(draftId, draftRepository).getHtml();
    }

    /**
     * Releases a Draft or shows validation errors.
     */
    @PostMapping("/drafts/{draftId}/release")
    @ResponseBody
    public String releaseDraft(
        @PathVariable String draftId,
        @ModelAttribute(name = "validityPeriod") ValidityPeriodDto periodDto,
        HttpServletResponse response
    ) {
        try {
            final var network = releasedRailNetworkService.releaseDraft(
                new ReleaseRailNetworkCommand(
                    draftId,
                    LocalDate.parse(periodDto.getStartDate()),
                    LocalDate.parse(periodDto.getEndDate())
                )
            );

            return redirectTo("/networks/" + network.getId().get(), response);

        } catch (ValidationException exception) {
            return new ReleaseView(draftId, draftRepository)
                .withValidationException(exception)
                .withValidityPeriod(periodDto)
                .getHtml();
        }
    }
}
