package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static com.ralfhenze.railplan.domain.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbQueriesTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(RailNetworkDraftMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void should_provide_all_draft_ids() {
        final RailNetworkDraftMongoDbRepository draftRepository =
            new RailNetworkDraftMongoDbRepository(mongoTemplate);
        final MongoDbQueries queries = new MongoDbQueries(mongoTemplate);
        final RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfPos)
            .withNewStation(hamburgHbfName, hamburgHbfPos)
            .withNewTrack(berlinHbfName, hamburgHbfName);
        final RailNetworkDraftId draftId1 = draftRepository
            .persist(draft).get().getId().get();
        final RailNetworkDraftId draftId2 = draftRepository
            .persist(draft.withRenamedStation(hamburgHbfName, frankfurtHbfName))
            .get().getId().get();

        List<String> draftIds = queries.getAllDraftIds();

        assertEquals(Arrays.asList(draftId1.toString(), draftId2.toString()), draftIds);
    }
}
