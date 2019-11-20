package com.ralfhenze.rms.railnetworkplanning.infrastructure;

import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.elements.TrainStationName;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraft;
import com.ralfhenze.rms.railnetworkplanning.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.rms.railnetworkplanning.infrastructure.persistence.RailNetworkDraftJpaRepository;

public class RailNetworkPlanningApp {

    public static void main(String[] args) {
        RailNetworkDraftRepository repo = new RailNetworkDraftJpaRepository();
        RailNetworkDraft draft = new RailNetworkDraft()
            .withNewStation(
                new TrainStationName("Berlin Hbf"),
                new GeoLocationInGermany(52.524927, 13.369348)
            )
            .withNewStation(
                new TrainStationName("Hamburg Hbf"),
                new GeoLocationInGermany(53.552596, 10.006727)
            );

        repo.persist(draft);
    }
}
