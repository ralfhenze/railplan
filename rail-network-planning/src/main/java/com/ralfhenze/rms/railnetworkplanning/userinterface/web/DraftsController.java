package com.ralfhenze.rms.railnetworkplanning.userinterface.web;

import com.ralfhenze.rms.railnetworkplanning.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.rms.railnetworkplanning.application.queries.Queries;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DraftsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/drafts")
    public String drafts(Model model) {
        final Queries queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());

        return "drafts";
    }

    @GetMapping("/drafts/new")
    public String createNewDraft() {
        final String id =
            new AddRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
            .addRailNetworkDraft()
            .get();

        return "redirect:/drafts";
    }
}
