package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkMongoDbRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MongoDbQueriesIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(RailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void providesAllNetworkIds() {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);
        final var queries = new MongoDbQueries(mongoTemplate);
        final var networkId1 = networkRepository.persist(new RailNetwork())
            .get().getId().get().toString();
        final var networkId2 = networkRepository.persist(new RailNetwork())
            .get().getId().get().toString();

        final var networkIds = queries.getAllNetworkIds();

        assertThat(networkIds).containsExactly(networkId1, networkId2);
    }
}
