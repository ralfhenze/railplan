package com.ralfhenze.railplan.userinterface.web.networks;

import com.ralfhenze.railplan.application.RailNetworkService;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NetworksController {

    private final Queries queries;
    private final RailNetworkService railNetworkService;

    @Autowired
    public NetworksController(
        final Queries queries,
        final RailNetworkService railNetworkService
    ) {
        this.queries = queries;
        this.railNetworkService = railNetworkService;
    }

    /**
     * Provides a list of all Networks.
     */
    @GetMapping({"/", "/networks"})
    @ResponseBody
    public String showNetworksPage() {
        return new NetworksView(queries.getAllNetworkIds())
            .getHtml()
            .render();
    }

    /**
     * Creates a new Network and redirects to it.
     */
    @GetMapping("/networks/new")
    public String addNetwork() {
        final var networkId = railNetworkService
            .addNetwork().get()
            .getId().get().toString();

        return "redirect:/networks/" + networkId;
    }

    /**
     * Deletes an existing Network and redirects to /networks.
     */
    @GetMapping("/networks/{networkId}/delete")
    public String deleteNetwork(@PathVariable String networkId) {
        railNetworkService.deleteNetwork(
            new DeleteRailNetworkCommand(networkId)
        );

        return "redirect:/networks";
    }
}
