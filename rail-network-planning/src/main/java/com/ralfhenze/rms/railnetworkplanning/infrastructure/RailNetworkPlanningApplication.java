package com.ralfhenze.rms.railnetworkplanning.infrastructure;

import com.ralfhenze.rms.railnetworkplanning.application.commands.ReleaseRailNetworkCommand;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.ReleasedRailNetworkMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class RailNetworkPlanningApplication implements CommandLineRunner {

    @Autowired
    RailNetworkDraftMongoDbRepository draftRepository;

    @Autowired
    ReleasedRailNetworkMongoDbRepository networkRepository;

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
        /*
        draft = draftRepository.persist(draft).get();
        System.out.println(draft.getId());

        new ReleaseRailNetworkCommand(draftRepository, networkRepository).releaseRailNetworkDraft(
            draft.getId().toString(),
            LocalDate.of(2019, 11, 19),
            LocalDate.of(2019, 11, 29)
        );

        networkRepository.getLastReleasedRailNetwork().ifPresent(
            n -> System.out.println(n.getPeriod())
        );
         */
    }
}
