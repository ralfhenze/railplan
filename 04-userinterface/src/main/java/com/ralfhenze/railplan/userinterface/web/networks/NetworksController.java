package com.ralfhenze.railplan.userinterface.web.networks;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.ReleasedRailNetworkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public String showNetworksListPage() {
        return new NetworksListView(queries.getAllNetworkIds()).getHtml().render();
    }

    /**
     * Provides a Network page with all information (Validity Period, Stations and Tracks).
     */
    @GetMapping("/networks/{networkId}")
    @ResponseBody
    public String showNetworkPage(@PathVariable String networkId) {
        final var network = networkRepository
            .getReleasedRailNetworkOfId(new ReleasedRailNetworkId(networkId));
        final var networkDto = new ReleasedRailNetworkDto(network);

        return new NetworkView(networkDto).getHtml().render();
    }
}
