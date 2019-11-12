package com.ralfhenze.rms.railnetworkplanning.domain;

import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocation;
import com.ralfhenze.rms.railnetworkplanning.domain.station.GeoLocationInGermany;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationId;
import com.ralfhenze.rms.railnetworkplanning.domain.station.StationName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RailNetworkDraftTest {

    @Test
    void should_ensure_unique_station_names() {
        RailNetworkDraft draft = new RailNetworkDraft(new RailNetworkDraftId("1"));
        GeoLocationInGermany location1 = new GeoLocationInGermany(new GeoLocation(51.0, 11.0));
        GeoLocationInGermany location2 = new GeoLocationInGermany(new GeoLocation(52.0, 12.0));
        GeoLocationInGermany location3 = new GeoLocationInGermany(new GeoLocation(53.0, 13.0));
        StationId firstStationId = draft.addStation(new StationName("First Station"), location1);
        StationId secondStationId = draft.addStation(new StationName("Second Station"), location2);

        assertThrows(Exception.class, () -> {
            draft.addStation(new StationName("First Station"), location3);
        });

        assertThrows(Exception.class, () -> {
            draft.renameStation(secondStationId, new StationName("First Station"));
        });
    }
}
