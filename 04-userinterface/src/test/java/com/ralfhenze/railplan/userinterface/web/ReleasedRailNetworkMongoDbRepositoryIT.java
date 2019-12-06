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

import static com.ralfhenze.railplan.domain.TestData.berlinHbf;
import static com.ralfhenze.railplan.domain.TestData.defaultPeriod;
import static com.ralfhenze.railplan.domain.TestData.hamburgHbf;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReleasedRailNetworkMongoDbRepositoryIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(ReleasedRailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void persistsGivenNetwork() {
        final var networkRepository = new ReleasedRailNetworkMongoDbRepository(mongoTemplate);
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            Lists.immutable.of(berlinHbf, hamburgHbf),
            Lists.immutable.of(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
        );
        networkRepository.add(network);

        final var loadedNetwork = networkRepository.getLastReleasedRailNetwork().get();

        assertThat(loadedNetwork.getStations()).hasSize(2);
        assertThat(loadedNetwork.getTracks()).hasSize(1);
    }
}
