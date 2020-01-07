package com.ralfhenze.railplan.userinterface.web.drafts;

import com.ralfhenze.railplan.application.RailNetworkDraftService;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DraftsController {

    private final Queries queries;
    private final RailNetworkDraftService railNetworkDraftService;

    @Autowired
    public DraftsController(
        final Queries queries,
        final RailNetworkDraftService railNetworkDraftService
    ) {
        this.queries = queries;
        this.railNetworkDraftService = railNetworkDraftService;
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
        final var draftId = railNetworkDraftService
            .addDraft().get()
            .getId().get().toString();

        return "redirect:/drafts/" + draftId;
    }

    /**
     * Deletes an existing Draft and redirects to /drafts.
     */
    @GetMapping("/drafts/{currentDraftId}/delete")
    public String deleteDraft(@PathVariable String currentDraftId) {
        railNetworkDraftService.deleteDraft(
            new DeleteRailNetworkDraftCommand(currentDraftId)
        );

        return "redirect:/drafts";
    }
}
