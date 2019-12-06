package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/drafts")
    public RailNetworkDraftDto addDraft() {
        final var addedDraft =
            new AddRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
                .addRailNetworkDraft()
                .get();

        return new RailNetworkDraftDto(addedDraft);
    }

    @GetMapping("/drafts/{draftId}")
    public RailNetworkDraftDto getDraft(@PathVariable String draftId) {
        return new MongoDbQueries(mongoTemplate).getDraftOfId(draftId);
    }

    @GetMapping("/drafts/{draftId}/stations")
    public List<TrainStationDto> getStations(@PathVariable String draftId) {
        return new MongoDbQueries(mongoTemplate).getDraftOfId(draftId).getStations();
    }

    @PostMapping("/drafts/{draftId}/stations")
    public TrainStationDto addStation(
        @PathVariable String draftId,
        @RequestBody TrainStationDto stationDto
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        final var addedStation = new AddTrainStationCommand(draftRepository).addTrainStation(
            draftId,
            stationDto.getName(),
            stationDto.getLatitude(),
            stationDto.getLongitude()
        );

        return new TrainStationDto(addedStation);
    }

    @GetMapping("/drafts/{draftId}/tracks")
    public List<RailwayTrackDto> getTracks(@PathVariable String draftId) {
        return new MongoDbQueries(mongoTemplate).getDraftOfId(draftId).getTracks();
    }

    @PostMapping("/drafts/{draftId}/tracks")
    public RailwayTrackDto createNewTrack(
        @PathVariable String draftId,
        @RequestBody RailwayTrackDto trackDto
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        final var addedTrack = new AddRailwayTrackCommand(draftRepository).addRailwayTrack(
            draftId,
            String.valueOf(trackDto.getFirstStationId()),
            String.valueOf(trackDto.getSecondStationId())
        );

        return new RailwayTrackDto(addedTrack.get());
    }
}
