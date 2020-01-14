package com.ralfhenze.railplan.userinterface.web.drafts;

import com.ralfhenze.railplan.application.RailNetworkDraftService;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public String showDraftsPage() {
        return new DraftsView().getHtml(queries.getAllDraftIds());
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
    @GetMapping("/drafts/{draftId}/delete")
    public String deleteDraft(@PathVariable String draftId) {
        railNetworkDraftService.deleteDraft(
            new DeleteRailNetworkDraftCommand(draftId)
        );

        return "redirect:/drafts";
    }
}
