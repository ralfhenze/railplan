package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
}
