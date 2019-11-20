package com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.entities.DraftEntity;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.entities.DraftStationEntity;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RailNetworkDraftJpaRepository implements RailNetworkDraftRepository {

    private EntityManager entityManager = Persistence
        .createEntityManagerFactory("rms-jpa")
        .createEntityManager();

    @Override
    public Optional<RailNetworkDraft> getRailNetworkDraftOfId(RailNetworkDraftId id) {
        DraftEntity draftEntity = entityManager
            .find(DraftEntity.class, Long.valueOf(id.toString()));

        if (draftEntity != null) {
            return Optional.of(draftEntity.toRailNetworkDraft());
        }

        return Optional.empty();
    }

    @Override
    public Optional<RailNetworkDraft> persist(RailNetworkDraft railNetworkDraft) {
        DraftEntity draftEntity = new DraftEntity();

        Set<DraftStationEntity> stations = railNetworkDraft.getStations().stream()
            .map(DraftStationEntity::new)
            .collect(Collectors.toSet());
        draftEntity.setStations(stations);

        entityManager.getTransaction().begin();
        stations.forEach(s -> entityManager.persist(s));
        entityManager.persist(draftEntity);
        entityManager.getTransaction().commit();

        return Optional.empty();
    }
}
