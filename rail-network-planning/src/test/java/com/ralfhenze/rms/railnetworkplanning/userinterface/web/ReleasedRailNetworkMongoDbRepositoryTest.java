package com.ralfhenze.rms.railnetworkplanning.userinterface.web;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.ReleasedRailNetworkMongoDbRepository;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.ralfhenze.rms.railnetworkplanning.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReleasedRailNetworkMongoDbRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(ReleasedRailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void should_persist_given_network() {
        final ReleasedRailNetworkMongoDbRepository networkRepository =
            new ReleasedRailNetworkMongoDbRepository(mongoTemplate);
        final ReleasedRailNetwork network = new ReleasedRailNetwork(
            defaultPeriod,
            Lists.immutable.of(berlinHbf, hamburgHbf),
            Lists.immutable.of(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
        );

        networkRepository.add(network).get();

        final ReleasedRailNetwork loadedNetwork = networkRepository
            .getLastReleasedRailNetwork().get();

        assertEquals(2, loadedNetwork.getStations().size());
        assertEquals(1, loadedNetwork.getTracks().size());
    }
}
