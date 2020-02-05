package com.ralfhenze.railplan.userinterface.web.api;

import com.ralfhenze.railplan.application.RailNetworkService;
import com.ralfhenze.railplan.application.RailwayTrackService;
import com.ralfhenze.railplan.application.TrainStationService;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackByStationIdCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDto;
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

    @GetMapping("/networks")
    public List<RailNetworkDto> getNetworks() {
        return new MongoDbQueries(mongoTemplate).getAllNetworks();
    }

    @PostMapping("/networks")
    public RailNetworkDto addNetwork() {
        final var addedNetwork =
            new RailNetworkService(new RailNetworkMongoDbRepository(mongoTemplate))
                .addNetwork()
                .get();

        return new RailNetworkDto(addedNetwork);
    }

    @GetMapping("/networks/{networkId}")
    public RailNetworkDto getNetwork(@PathVariable String networkId) {
        return new MongoDbQueries(mongoTemplate).getNetworkOfId(networkId);
    }

    @GetMapping("/networks/{networkId}/stations")
    public List<TrainStationDto> getStations(@PathVariable String networkId) {
        return new MongoDbQueries(mongoTemplate).getNetworkOfId(networkId).getStations();
    }

    @PostMapping("/networks/{networkId}/stations")
    public TrainStationDto addStation(
        @PathVariable String networkId,
        @RequestBody TrainStationDto stationDto
    ) {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);

        new TrainStationService(networkRepository).addStationToNetwork(
            new AddTrainStationCommand(
                networkId,
                stationDto.getName(),
                stationDto.getLatitude(),
                stationDto.getLongitude()
            )
        );

        final var addedStation = networkRepository
            .getRailNetworkOfId(new RailNetworkId(networkId))
            .getStations()
            .getLastOptional()
            .get();

        return new TrainStationDto(addedStation);
    }

    @GetMapping("/networks/{networkId}/tracks")
    public List<RailwayTrackDto> getTracks(@PathVariable String networkId) {
        return new MongoDbQueries(mongoTemplate).getNetworkOfId(networkId).getTracks();
    }

    @PostMapping("/networks/{networkId}/tracks")
    public RailwayTrackDto createNewTrack(
        @PathVariable String networkId,
        @RequestBody RailwayTrackDto trackDto
    ) {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);

        new RailwayTrackService(networkRepository).addTrackByStationId(
            new AddRailwayTrackByStationIdCommand(
                networkId,
                trackDto.getFirstStationId(),
                trackDto.getSecondStationId()
            )
        );

        final var addedTrack = networkRepository
            .getRailNetworkOfId(new RailNetworkId(networkId))
            .getTracks()
            .getLastOptional()
            .get();

        return new RailwayTrackDto(addedTrack);
    }
}
