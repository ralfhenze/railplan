package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetwork;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkId;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.BERLIN_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.HAMBURG_HBF;
import static com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation.ERFURT_HBF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RailNetworkMongoDbRepositoryIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void clearRepository() {
        mongoTemplate.dropCollection(RailNetworkMongoDbRepository.COLLECTION_NAME);
    }

    @Test
    public void persistsAndLoadsNetwork() {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);
        final var network = getExampleNetwork();
        final var persistedNetwork = networkRepository.persist(network).get();
        final var networkId = persistedNetwork.getId().get();

        final var loadedNetwork = networkRepository.getRailNetworkOfId(networkId);

        assertThat(loadedNetwork.getStations()).hasSize(2);
        assertThat(loadedNetwork.getTracks()).hasSize(1);
    }

    @Test
    public void throwsExceptionWhenLoadingNonExistentNetwork() {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
            networkRepository.getRailNetworkOfId(new RailNetworkId("123"))
        );
    }

    @Test
    public void updatesPersistedNetwork() {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);
        final var network = getExampleNetwork();
        final var persistedNetwork = networkRepository.persist(network).get();
        final var updatedNetwork = persistedNetwork
            .addStations(ERFURT_HBF)
            .addTrackBetween(ERFURT_HBF, BERLIN_HBF);

        networkRepository.persist(updatedNetwork);

        final var networkId = persistedNetwork.getId().get();
        final var loadedNetwork = networkRepository
            .getRailNetworkOfId(networkId);
        final var numberOfPersistedNetworks = mongoTemplate
            .findAll(RailNetworkDto.class, RailNetworkMongoDbRepository.COLLECTION_NAME)
            .size();

        assertThat(numberOfPersistedNetworks).isEqualTo(1);
        assertThat(loadedNetwork.getStations()).hasSize(3);
        assertThat(loadedNetwork.getTracks()).hasSize(2);
    }

    @Test
    public void deletesPersistedNetwork() {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);
        final var network = getExampleNetwork();
        final var persistedNetwork = networkRepository.persist(network).get();
        final var networkId = persistedNetwork.getId().get();

        networkRepository.deleteRailNetworkOfId(networkId);

        final var numberOfPersistedNetworks = mongoTemplate
            .findAll(RailNetworkDto.class, RailNetworkMongoDbRepository.COLLECTION_NAME)
            .size();

        assertThat(numberOfPersistedNetworks).isEqualTo(0);
    }

    @Test
    public void throwsExceptionWhenDeletingNonExistentNetwork() {
        final var networkRepository = new RailNetworkMongoDbRepository(mongoTemplate);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
            networkRepository.deleteRailNetworkOfId(new RailNetworkId("123"))
        );
    }

    private RailNetwork getExampleNetwork() {
        return new RailNetwork()
            .addStations(BERLIN_HBF, HAMBURG_HBF)
            .addTrackBetween(BERLIN_HBF, HAMBURG_HBF);
    }
}
