package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class RailPlanApplication {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RailNetworkDraftRepository railNetworkDraftRepository;

    public static void main(String[] args) {
        SpringApplication.run(RailPlanApplication.class, args);
    }

    @Bean
    public Queries getQueries() {
        return new MongoDbQueries(mongoTemplate);
    }

    @Bean
    public RailNetworkDraftRepository getRailNetworkDraftRepository() {
        return new RailNetworkDraftMongoDbRepository(mongoTemplate);
    }

    @Bean
    public AddRailNetworkDraftCommand getAddRailNetworkDraftCommand() {
        return new AddRailNetworkDraftCommand(railNetworkDraftRepository);
    }

    @Bean
    public AddRailwayTrackCommand getAddRailwayTrackCommand() {
        return new AddRailwayTrackCommand(railNetworkDraftRepository);
    }

    @Bean
    public AddTrainStationCommand getAddTrainStationCommand() {
        return new AddTrainStationCommand(railNetworkDraftRepository);
    }

    @Bean
    public DeleteRailNetworkDraftCommand getDeleteRailNetworkDraftCommand() {
        return new DeleteRailNetworkDraftCommand(railNetworkDraftRepository);
    }

    @Bean
    public DeleteRailwayTrackCommand getDeleteRailwayTrackCommand() {
        return new DeleteRailwayTrackCommand(railNetworkDraftRepository);
    }

    @Bean
    public DeleteTrainStationCommand getDeleteTrainStationCommand() {
        return new DeleteTrainStationCommand(railNetworkDraftRepository);
    }

    @Bean
    public UpdateTrainStationCommand getUpdateTrainStationCommand() {
        return new UpdateTrainStationCommand(railNetworkDraftRepository);
    }
}
