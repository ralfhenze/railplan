package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        final var queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());

        return "drafts";
    }

    @GetMapping("/drafts/{draftId}")
    public String draft(@PathVariable String draftId, Model model) {
        final var queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());
        model.addAttribute("currentDraftId", draftId);

        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);
        draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId(draftId))
            .map(RailNetworkDraftDto::new)
            .ifPresent(draftDto -> {
                model.addAttribute("currentDraftDto", draftDto);
                final Map<Integer, String> stationNames = draftDto
                    .getStations()
                    .stream()
                    .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));
                model.addAttribute("stationNames", stationNames);
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

        model.addAttribute("newStation", new TrainStationDto());
        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @PostMapping("/drafts/{draftId}/stations/new")
    public String createNewStation(
        @PathVariable String draftId,
        @ModelAttribute TrainStationDto stationDto
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        new AddTrainStationCommand(draftRepository).addTrainStation(
            draftId,
            stationDto.getName(),
            stationDto.getLatitude(),
            stationDto.getLongitude()
        );

        return "redirect:/drafts/{draftId}";
    }

    @PostMapping("/drafts/{draftId}/tracks/new")
    public String createNewTrack(
        @PathVariable String draftId,
        @ModelAttribute RailwayTrackDto trackDto
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        new AddRailwayTrackCommand(draftRepository).addRailwayTrack(
            draftId,
            String.valueOf(trackDto.getFirstStationId()),
            String.valueOf(trackDto.getSecondStationId())
        );

        return "redirect:/drafts/{draftId}";
    }

    @GetMapping("/drafts/new")
    public String createNewDraft() {
        final var draftId =
            new AddRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
            .addRailNetworkDraft()
            .get();

        return "redirect:/drafts/" + draftId;
    }
}
