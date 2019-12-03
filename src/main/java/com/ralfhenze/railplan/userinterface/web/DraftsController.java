package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
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

    @GetMapping("/drafts/new")
    public String addDraft() {
        final var draftId =
            new AddRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
                .addRailNetworkDraft().get()
                .getId().get().toString();

        return "redirect:/drafts/" + draftId;
    }

    @GetMapping("/drafts/{currentDraftId}")
    public String draft(@PathVariable String currentDraftId, Model model) {
        setModelAttributes(currentDraftId, model);

        model.addAttribute("newStation", new TrainStationDto());
        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @PostMapping("/drafts/{currentDraftId}/stations/new")
    public String createNewStation(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newStation") TrainStationDto stationDto,
        Model model
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        try {
            new AddTrainStationCommand(draftRepository).addTrainStation(
                currentDraftId,
                stationDto.getName(),
                stationDto.getLatitude(),
                stationDto.getLongitude()
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model);
            model.addAttribute("newTrack", new RailwayTrackDto());
            model.addAttribute("stationErrors", exception.getErrorMessages());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    @PostMapping("/drafts/{currentDraftId}/tracks/new")
    public String createNewTrack(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newTrack") RailwayTrackDto trackDto,
        Model model
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        try {
            new AddRailwayTrackCommand(draftRepository).addRailwayTrack(
                currentDraftId,
                String.valueOf(trackDto.getFirstStationId()),
                String.valueOf(trackDto.getSecondStationId())
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model);
            model.addAttribute("newStation", new TrainStationDto());
            model.addAttribute("trackErrors", exception.getErrorMessages());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    private void setModelAttributes(String currentDraftId, Model model) {
        final var queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());

        final var draftDto = new RailNetworkDraftMongoDbRepository(mongoTemplate)
            .getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId))
            .map(RailNetworkDraftDto::new);

        if (draftDto.isPresent()) {
            model.addAttribute("currentDraftDto", draftDto.get());

            final Map<Integer, String> stationNames = draftDto.get()
                .getStations()
                .stream()
                .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));
            model.addAttribute("stationNames", stationNames);

            final List<List<String>> tracksWithStationNames = draftDto.get()
                .getTracks()
                .stream()
                .map(trackDto -> Arrays.asList(
                    stationNames.get(trackDto.getFirstStationId()),
                    stationNames.get(trackDto.getSecondStationId()))
                )
                .collect(Collectors.toList());
            model.addAttribute("tracks", tracksWithStationNames);
        }
    }
}
