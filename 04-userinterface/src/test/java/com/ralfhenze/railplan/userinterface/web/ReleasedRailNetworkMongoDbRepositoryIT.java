package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.infrastructure.persistence.ReleasedRailNetworkMongoDbRepository;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.ralfhenze.railplan.userinterface.web.TestData.BERLIN_HBF;
import static com.ralfhenze.railplan.userinterface.web.TestData.DEFAULT_PERIOD;
import static com.ralfhenze.railplan.userinterface.web.TestData.HAMBURG_HBF;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReleasedRailNetworkMongoDbRepositoryIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(ReleasedRailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void persistsAndLoadsNetwork() {
        final var networkRepository = new ReleasedRailNetworkMongoDbRepository(mongoTemplate);
        final var network = getBerlinHamburgNetwork();
        final var persistedNetwork = networkRepository.add(network);

        final var loadedNetwork = networkRepository
            .getReleasedRailNetworkOfId(persistedNetwork.getId().get());

        assertThat(loadedNetwork.getStations()).hasSize(2);
        assertThat(loadedNetwork.getTracks()).hasSize(1);
    }

    @Test
    public void providesLastReleasedNetwork() {
        final var networkRepository = new ReleasedRailNetworkMongoDbRepository(mongoTemplate);
        final var network = getBerlinHamburgNetwork();
        networkRepository.add(network);

        final var loadedNetwork = networkRepository.getLastReleasedRailNetwork().get();

        assertThat(loadedNetwork.getStations()).hasSize(2);
        assertThat(loadedNetwork.getTracks()).hasSize(1);
    }

    private ReleasedRailNetwork getBerlinHamburgNetwork() {
        return new ReleasedRailNetwork(
            DEFAULT_PERIOD,
            Lists.immutable.of(BERLIN_HBF, HAMBURG_HBF),
            Lists.immutable.of(new RailwayTrack(BERLIN_HBF.getId(), HAMBURG_HBF.getId()))
        );
    }
}
