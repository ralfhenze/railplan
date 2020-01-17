package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.elements.RailwayTrack;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.ReleasedRailNetworkMongoDbRepository;
import org.eclipse.collections.api.factory.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbf;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.defaultPeriod;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbf;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfName;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MongoDbQueriesIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(RailNetworkDraftMongoDbRepository.COLLECTION_NAME);
        mongoTemplate.dropCollection(ReleasedRailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void providesAllDraftIds() {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);
        final var queries = new MongoDbQueries(mongoTemplate);
        final var draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfLat, berlinHbfLng)
            .withNewStation(hamburgHbfName, hamburgHbfLat, hamburgHbfLng)
            .withNewTrack(berlinHbfName, hamburgHbfName);
        final var draftId1 = draftRepository
            .persist(draft).get().getId().get().toString();
        final var draftId2 = draftRepository
            .persist(draft.withUpdatedStation(1, potsdamHbfName, potsdamHbfLat, potsdamHbfLng))
            .get().getId().get().toString();

        final var draftIds = queries.getAllDraftIds();

        assertThat(draftIds).containsExactly(draftId1, draftId2);
    }

    @Test
    public void providesAllNetworkIds() {
        final var networkRepository = new ReleasedRailNetworkMongoDbRepository(mongoTemplate);
        final var queries = new MongoDbQueries(mongoTemplate);
        final var network = new ReleasedRailNetwork(
            defaultPeriod,
            Lists.immutable.of(berlinHbf, hamburgHbf),
            Lists.immutable.of(new RailwayTrack(berlinHbf.getId(), hamburgHbf.getId()))
        );
        final var networkId1 = networkRepository
            .add(network).getId().get().toString();
        final var networkId2 = networkRepository
            .add(network).getId().get().toString();

        final var networkIds = queries.getAllNetworkIds();

        assertThat(networkIds).containsExactly(networkId1, networkId2);
    }
}
