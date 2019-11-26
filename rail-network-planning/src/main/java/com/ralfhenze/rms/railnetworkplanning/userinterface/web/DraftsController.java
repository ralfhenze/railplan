package com.ralfhenze.rms.railnetworkplanning.userinterface.web;

import com.ralfhenze.rms.railnetworkplanning.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.rms.railnetworkplanning.application.queries.Queries;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/drafts/{draftId}")
    public String draft(@PathVariable String draftId, Model model) {
        final Queries queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());
        model.addAttribute("currentDraftId", draftId);

        final RailNetworkDraftRepository draftRepository =
            new RailNetworkDraftMongoDbRepository(mongoTemplate);
        draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId(draftId))
            .map(RailNetworkDraftDto::new)
            .ifPresent(draftDto -> {
                model.addAttribute("currentDraftDto", draftDto);
                final Map<Integer, String> stationNames = draftDto
                    .getStations()
                    .stream()
                    .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));
                final List<List<String>> tracksWithStationNames = draftDto
                    .getTracks()
                    .stream()
                    .map(trackDto -> Arrays.asList(
                        stationNames.get(trackDto.getFirstStationId()),
                        stationNames.get(trackDto.getSecondStationId()))
                    )
                    .collect(Collectors.toList());
                model.addAttribute("tracks", tracksWithStationNames);
            });

        return "drafts";
    }

    @GetMapping("/drafts/new")
    public String createNewDraft() {
        final String draftId =
            new AddRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
            .addRailNetworkDraft()
            .get();

        return "redirect:/drafts/" + draftId;
    }
}
