package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
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
public class RailNetworkDraftMongoDbRepositoryTest {

    @Autowired
    RailNetworkDraftMongoDbRepository draftRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(RailNetworkDraftMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void should_persist_given_draft() {
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);

        final RailNetworkDraft persistedDraft = draftRepository.persist(draft).get();
        final RailNetworkDraftId draftId = persistedDraft.getId().get();

        final RailNetworkDraft loadedDraft = draftRepository
            .getRailNetworkDraftOfId(draftId).get();

        // TODO: add deeper comparison of retrieved object graph
        assertEquals(2, loadedDraft.getStations().size());
        assertEquals(1, loadedDraft.getTracks().size());
    }
}
