package com.ralfhenze.railplan.userinterface.web.networks;

import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.ReleasedRailNetworkDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Provides a Network page with all information (Validity Period, Stations and Tracks).
     */
    @GetMapping("/networks/{currentNetworkId}")
    public String draft(@PathVariable String currentNetworkId, Model model) {
        final var network = networkRepository
            .getReleasedRailNetworkOfId(new ReleasedRailNetworkId(currentNetworkId));
        final var networkDto = new ReleasedRailNetworkDto(network);

        model.addAttribute("networkIds", queries.getAllNetworkIds());
        model.addAttribute("currentNetwork", network);
        model.addAttribute("tracks", getTracksWithStationNames(networkDto));

        return "networks";
    }

    private List<List<String>> getTracksWithStationNames(
        final ReleasedRailNetworkDto networkDto
    ) {
        final var stationNames = networkDto.getStations().stream()
            .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));

        return networkDto.getTracks().stream()
            .map(trackDto -> List.of(
                stationNames.get(trackDto.getFirstStationId()),
                stationNames.get(trackDto.getSecondStationId())
            ))
            .collect(Collectors.toList());
    }
}
