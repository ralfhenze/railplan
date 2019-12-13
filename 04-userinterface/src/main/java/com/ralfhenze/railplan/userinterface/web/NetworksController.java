package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NetworksController {

    private final Queries queries;
    private final ReleasedRailNetworkRepository networkRepository;

    @Autowired
    public NetworksController(
        final Queries queries,
        final ReleasedRailNetworkRepository networkRepository
    ) {
        this.queries = queries;
        this.networkRepository = networkRepository;
    }

    /**
     * Provides a list of all Networks.
     */
    @GetMapping("/networks")
    public String drafts(Model model) {
        model.addAttribute("networkIds", queries.getAllNetworkIds());

        return "networks";
    }
}
