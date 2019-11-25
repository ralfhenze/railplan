package com.ralfhenze.rms.railnetworkplanning.infrastructure;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RailNetworkPlanningApplication implements CommandLineRunner {

    @Autowired
    RailNetworkDraftMongoDbRepository draftRepository;

    public static void main(String[] args) {
        SpringApplication.run(RailNetworkPlanningApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TrainStationName berlinHbf = new TrainStationName("Berlin Hbf");
        TrainStationName hamburgHbf = new TrainStationName("Hamburg Hbf");

        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(
                berlinHbf,
                new GeoLocationInGermany(52.524927, 13.369348)
            )
            .withNewStation(
                hamburgHbf,
                new GeoLocationInGermany(53.552596, 10.006727)
            )
            .withNewTrack(berlinHbf, hamburgHbf);

        draftRepository.persist(draft);

        draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId("5ddc0c5a8b46e57f6f5d3bfe"))
            .ifPresent(loaded -> System.out.println(loaded.getStations()));
    }
}
