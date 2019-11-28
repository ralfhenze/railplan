package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/drafts")
    public List<RailNetworkDraftDto> getDrafts() {
        return new MongoDbQueries(mongoTemplate).getAllDrafts();
    }

    @GetMapping("/drafts/{draftId}")
    public RailNetworkDraftDto getDraft(@PathVariable String draftId) {
        return new MongoDbQueries(mongoTemplate).getDraftOfId(draftId);
    }

    @GetMapping("/drafts/{draftId}/stations")
    public List<TrainStationDto> getStations(@PathVariable String draftId) {
        return new MongoDbQueries(mongoTemplate).getDraftOfId(draftId).getStations();
    }

    @PostMapping("/drafts/{currentDraftId}/stations")
    public TrainStationDto addStation(
        @PathVariable String currentDraftId,
        @RequestBody TrainStationDto stationDto
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        final var addedStation = new AddTrainStationCommand(draftRepository).addTrainStation(
            currentDraftId,
            stationDto.getName(),
            stationDto.getLatitude(),
            stationDto.getLongitude()
        );

        return new TrainStationDto(addedStation.get());
    }
}
