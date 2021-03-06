package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.RailNetworkService;
import com.ralfhenze.railplan.application.RailwayTrackService;
import com.ralfhenze.railplan.application.TrainStationService;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkMongoDbRepository;
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
    private RailNetworkRepository railNetworkRepository;

    public static void main(String[] args) {
        SpringApplication.run(RailPlanApplication.class, args);
    }

    @Bean
    public Queries getQueries() {
        return new MongoDbQueries(mongoTemplate);
    }

    @Bean
    public RailNetworkRepository getRailNetworkRepository() {
        return new RailNetworkMongoDbRepository(mongoTemplate);
    }

    @Bean
    public RailNetworkService getRailNetworkService() {
        return new RailNetworkService(railNetworkRepository);
    }

    @Bean
    public RailwayTrackService getRailwayTrackService() {
        return new RailwayTrackService(railNetworkRepository);
    }

    @Bean
    public TrainStationService getTrainStationService() {
        return new TrainStationService(railNetworkRepository);
    }
}
