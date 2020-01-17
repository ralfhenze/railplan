package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.berlinHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.hamburgHbfName;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfLat;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfLng;
import static com.ralfhenze.railplan.userinterface.web.TestData.potsdamHbfName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RailNetworkDraftMongoDbRepositoryIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(RailNetworkDraftMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void persistsAndLoadsDraft() {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);
        final var draft = getExampleDraft();
        final var persistedDraft = draftRepository.persist(draft).get();
        final var draftId = persistedDraft.getId().get();

        final var loadedDraft = draftRepository.getRailNetworkDraftOfId(draftId);

        assertThat(loadedDraft.getStations()).hasSize(2);
        assertThat(loadedDraft.getTracks()).hasSize(1);
    }

    @Test
    public void throwsExceptionWhenLoadingNonExistentDraft() {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
            draftRepository.getRailNetworkDraftOfId(new RailNetworkDraftId("123"))
        );
    }

    @Test
    public void updatesPersistedDraft() {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);
        final var draft = getExampleDraft();
        final var persistedDraft = draftRepository.persist(draft).get();
        final var updatedDraft = persistedDraft
            .withNewStation(potsdamHbfName, potsdamHbfLat, potsdamHbfLng)
            .withNewTrack(potsdamHbfName, berlinHbfName);

        draftRepository.persist(updatedDraft);

        final var draftId = persistedDraft.getId().get();
        final var loadedDraft = draftRepository
            .getRailNetworkDraftOfId(draftId);
        final var numberOfPersistedDrafts = mongoTemplate
            .findAll(RailNetworkDraftDto.class, RailNetworkDraftMongoDbRepository.COLLECTION_NAME)
            .size();

        assertThat(numberOfPersistedDrafts).isEqualTo(1);
        assertThat(loadedDraft.getStations()).hasSize(3);
        assertThat(loadedDraft.getTracks()).hasSize(2);
    }

    @Test
    public void deletesPersistedDraft() {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);
        final var draft = getExampleDraft();
        final var persistedDraft = draftRepository.persist(draft).get();
        final var draftId = persistedDraft.getId().get();

        draftRepository.deleteRailNetworkDraftOfId(draftId);

        final var numberOfPersistedDrafts = mongoTemplate
            .findAll(RailNetworkDraftDto.class, RailNetworkDraftMongoDbRepository.COLLECTION_NAME)
            .size();

        assertThat(numberOfPersistedDrafts).isEqualTo(0);
    }

    @Test
    public void throwsExceptionWhenDeletingNonExistentDraft() {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
            draftRepository.deleteRailNetworkDraftOfId(new RailNetworkDraftId("123"))
        );
    }

    private RailNetworkDraft getExampleDraft() {
        return new RailNetworkDraft()
            .withNewStation(berlinHbfName, berlinHbfLat, berlinHbfLng)
            .withNewStation(hamburgHbfName, hamburgHbfLat, hamburgHbfLng)
            .withNewTrack(berlinHbfName, hamburgHbfName);
    }
}
