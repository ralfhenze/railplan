package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.RailNetworkDraftService;
import com.ralfhenze.railplan.application.RailwayTrackService;
import com.ralfhenze.railplan.application.TrainStationService;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkDraftRepository;
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
    public RailNetworkDraftService getRailNetworkDraftService() {
        return new RailNetworkDraftService(railNetworkDraftRepository);
    }

    @Bean
    public RailwayTrackService getRailwayTrackService() {
        return new RailwayTrackService(railNetworkDraftRepository);
    }

    @Bean
    public TrainStationService getTrainStationService() {
        return new TrainStationService(railNetworkDraftRepository);
    }
}
